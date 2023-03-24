package com.itanoji.time.cutter.service;

import com.itanoji.time.cutter.domain.JwtRequest;
import com.itanoji.time.cutter.domain.JwtResponse;
import com.itanoji.time.cutter.model.User;
import com.itanoji.time.cutter.repository.UserRepository;
import com.itanoji.time.cutter.security.JwtAuthentication;
import com.itanoji.time.cutter.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    private final Map<String, String> refreshStorage = new HashMap<>();
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        final User user = Optional.ofNullable(userRepository.findByLogin(authRequest.getLogin()))
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtTokenProvider.generateAccessToken(user);
            final String refreshToken = jwtTokenProvider.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtTokenProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = Optional.of(userRepository.findByLogin(login))
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtTokenProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtTokenProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = Optional.of(userRepository.findByLogin(login))
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtTokenProvider.generateAccessToken(user);
                final String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}

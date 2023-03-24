package com.itanoji.time.cutter.rest;

import com.itanoji.time.cutter.Utils.Validation;
import com.itanoji.time.cutter.domain.ErrorMessage;
import com.itanoji.time.cutter.domain.JwtRequest;
import com.itanoji.time.cutter.domain.JwtResponse;
import com.itanoji.time.cutter.domain.UserDAO;
import com.itanoji.time.cutter.model.User;
import com.itanoji.time.cutter.repository.UserRepository;
import com.itanoji.time.cutter.security.JwtTokenProvider;
import com.itanoji.time.cutter.service.AuthService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) {
        try {
            final JwtResponse token = authService.login(authRequest);
            return ResponseEntity.ok(token);
        } catch (AuthException e) {
            return ResponseEntity.status(401).body(new ErrorMessage("Неправильный логин или пароль!"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDAO userDAO) {
        if(userDAO.getLogin() == null || userDAO.getEmail() == null || userDAO.getPassword() == null) {
            return new ResponseEntity<>("Некорректные данные!", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByLogin(userDAO.getLogin())) {
            return new ResponseEntity<>("Пользователь с таким логином уже существует!", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(userDAO.getEmail())) {
            return new ResponseEntity<>("Пользователь с такой почтой уже существует!", HttpStatus.BAD_REQUEST);
        }

        String loginValidationError = Validation.validateLogin(userDAO.getLogin());
        if(loginValidationError != null) {
            return new ResponseEntity<>(new ErrorMessage(loginValidationError), HttpStatus.BAD_REQUEST);
        }

        String passwordValidationError = Validation.validatePassword(userDAO.getPassword());
        if(passwordValidationError != null) {
            return new ResponseEntity<>(new ErrorMessage(passwordValidationError), HttpStatus.BAD_REQUEST);
        }

        User user = new User(null, userDAO.getLogin(), userDAO.getPassword(), userDAO.getEmail());
        user.setPassword(user.getPassword());

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getLogin()).toUri();

        return ResponseEntity.created(location).body("Вы успешно зарегистрировались!");
    }
}

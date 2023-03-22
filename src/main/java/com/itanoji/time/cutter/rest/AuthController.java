package com.itanoji.time.cutter.rest;

import com.itanoji.time.cutter.domain.UserPojo;
import com.itanoji.time.cutter.model.User;
import com.itanoji.time.cutter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserPojo userPojo) {
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserPojo userPojo) {
        if (userRepository.existsByLogin(userPojo.getLogin())) {
            return new ResponseEntity<>("Пользователь с таким логином уже существует!", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(userPojo.getEmail())) {
            return new ResponseEntity<>("Пользователь с такой почтой уже существует!", HttpStatus.BAD_REQUEST);
        }

        User user = new User(null, userPojo.getLogin(), userPojo.getEmail(), userPojo.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getLogin()).toUri();

        return ResponseEntity.created(location).body("Вы успешно зарегистрировались!");
    }
}

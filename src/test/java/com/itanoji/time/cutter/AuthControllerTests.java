package com.itanoji.time.cutter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itanoji.time.cutter.domain.JwtRequest;
import com.itanoji.time.cutter.domain.JwtResponse;
import com.itanoji.time.cutter.domain.UserDAO;
import com.itanoji.time.cutter.model.User;
import com.itanoji.time.cutter.repository.UserRepository;
import com.itanoji.time.cutter.service.AuthService;
import jakarta.security.auth.message.AuthException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testLoginAccepted() throws Exception {
        String username = "test";
        String password = "password";
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setLogin(username);
        jwtRequest.setPassword(password);

        // Мокаем ответ сервиса
        when(authService.login(any(JwtRequest.class)))
                .thenReturn(new JwtResponse("dummy_token", "dummy_token"));

        // Преобразуем наш JwtRequest в JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jwtRequestJson = objectMapper.writeValueAsString(jwtRequest);

        // Выполняем запрос
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jwtRequestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginDenied() throws Exception {
        String username = "test";
        String password = "password";
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setLogin(username);
        jwtRequest.setPassword(password);

        // Мокаем ответ сервиса
        when(authService.login(any(JwtRequest.class))).thenThrow(new AuthException());

        // Преобразуем наш JwtRequest в JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jwtRequestJson = objectMapper.writeValueAsString(jwtRequest);

        // Выполняем запрос
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jwtRequestJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        UserDAO userDAO = new UserDAO("a","b@gmail.com","a12345678");
        User user = new User();
        user.setLogin("a");
        user.setEmail("b");
        user.setPassword("c");

        //Мокаем userRepository
        when(userRepository.existsByLogin(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDAO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testRegisterLoginExists() throws Exception {
        UserDAO userDAO = new UserDAO("a","b@gmail.com","a12345678");
        User user = new User();
        user.setLogin("a");
        user.setEmail("b");
        user.setPassword("c");

        //Мокаем userRepository
        when(userRepository.existsByLogin(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDAO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterEmailExists() throws Exception {
        UserDAO userDAO = new UserDAO("a","b@gmail.com","a12345678");
        User user = new User();
        user.setLogin("a");
        user.setEmail("b");
        user.setPassword("c");

        //Мокаем userRepository
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDAO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterIncorrectPassword() throws Exception {
        UserDAO userDAO = new UserDAO("a","b@gmail.com","a");
        User user = new User();
        user.setLogin("a");
        user.setEmail("b");
        user.setPassword("c");

        //Мокаем userRepository
        when(userRepository.existsByLogin(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDAO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterIncorrectLogin() throws Exception {
        UserDAO userDAO = new UserDAO("___+","b@gmail.com","a224254544");
        User user = new User();
        user.setLogin("a");
        user.setEmail("b");
        user.setPassword("c");

        //Мокаем userRepository
        when(userRepository.existsByLogin(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDAO)))
                .andExpect(status().isBadRequest());
    }
}

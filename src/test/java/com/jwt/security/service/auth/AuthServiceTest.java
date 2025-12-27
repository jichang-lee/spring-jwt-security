package com.jwt.security.service.auth;

import com.jwt.security.domain.User;
import com.jwt.security.exception.EmailAlreadyExistsException;
import com.jwt.security.repository.UserRepository;
import com.jwt.security.request.auth.SignupRequest;
import com.jwt.security.service.AuthService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    public void signUpSuccess() throws Exception {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
                .email("jichang@naver.com")
                .password("1234214")
                .name("삼지창")
                .build();

        authService.signUp(signupRequest);

        //when
        User user = userRepository.findByEmail("jichang@naver.com")
                .orElseThrow(EmailAlreadyExistsException::new);

        //then
        assertEquals(1,userRepository.count());
        assertEquals("jichang@naver.com",user.getEmail());
        assertTrue(passwordEncoder.matches("1234214",user.getPassword()));
        assertEquals("삼지창",user.getName());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 둥복")
    public void signUpFail() throws Exception {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
                .email("jichang@naver.com")
                .password("1234214")
                .name("삼지창")
                .build();

        authService.signUp(signupRequest);

        //when&then
        SignupRequest existUser = SignupRequest.builder()
                .email("jichang@naver.com")
                .password("exist1234")
                .name("감자창")
                .build();

        assertThrows(EmailAlreadyExistsException.class,
                () -> authService.signUp(existUser));
        assertEquals(1,userRepository.count());
    }
}

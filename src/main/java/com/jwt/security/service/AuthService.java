package com.jwt.security.service;

import com.jwt.security.domain.User;
import com.jwt.security.exception.EmailAlreadyExistsException;
import com.jwt.security.repository.UserRepository;
import com.jwt.security.request.auth.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void signUp(SignupRequest signupRequest) {
       if(userRepository.findByEmail(signupRequest.getEmail()).isPresent()){
           log.error("회원가입 중복된 이메일 ={}",signupRequest.getEmail());
           throw new EmailAlreadyExistsException();
       }
        String encode = passwordEncoder.encode(signupRequest.getPassword());

        User userBuild = User.builder()
                .email(signupRequest.getEmail())
                .password(encode)
                .name(signupRequest.getName())
                .build();
        userRepository.save(userBuild);
    }
}

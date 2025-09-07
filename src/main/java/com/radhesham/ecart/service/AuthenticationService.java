package com.radhesham.ecart.service;

import com.radhesham.ecart.request.LoginUserRequest;
import com.radhesham.ecart.request.RegisterUserRequest;
import com.radhesham.ecart.entity.UserEntity;
import com.radhesham.ecart.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity signup(RegisterUserRequest input) {
        UserEntity user = new UserEntity();
        user.setName(input.getName());
        user.setEmail(input.getEmail());
        user.setPhone(input.getPhone());
        user.setEnable(1);
        user.setCredentialNonExpired(1);
        user.setAccountNonExpired(1);
        user.setAccountNonLock(1);
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(user);
    }

    public UserEntity authenticate(LoginUserRequest input) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }
}

package com.vinicius.dev.pulgattiwather.user;

import com.vinicius.dev.pulgattiwather.common.exception.DuplicateEmailException;
import com.vinicius.dev.pulgattiwather.common.exception.InvalidCredentialsException;
import com.vinicius.dev.pulgattiwather.security.JwtService;
import com.vinicius.dev.pulgattiwather.user.dto.AuthResponse;
import com.vinicius.dev.pulgattiwather.user.dto.LoginRequest;
import com.vinicius.dev.pulgattiwather.user.dto.RegisterRequest;
import com.vinicius.dev.pulgattiwather.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        return userMapper.toResponse(userRepository.save(user));
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException();
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), java.util.List.of());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, userMapper.toResponse(user));
    }
}

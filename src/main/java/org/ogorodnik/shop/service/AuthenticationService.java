package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.api.dto.AuthenticationRequest;
import org.ogorodnik.shop.api.dto.AuthenticationResponse;
import org.ogorodnik.shop.api.dto.RegisterRequest;
import org.ogorodnik.shop.security.utils.JwtUtils;
import org.ogorodnik.shop.entity.Credentials;
import org.ogorodnik.shop.entity.Role;
import org.ogorodnik.shop.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Credentials credentials = Credentials.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .role(Role.USER)
                .build();
        userRepository.save(credentials);

        var jwtToken = jwtUtils.generateToken(credentials);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String user = request.getUsername();
        String password = request.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user, password));
        var credentials = userRepository.findByUsernameIgnoreCase(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtUtils.generateToken(credentials);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}

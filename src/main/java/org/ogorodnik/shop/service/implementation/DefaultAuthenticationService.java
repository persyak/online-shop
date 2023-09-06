package org.ogorodnik.shop.service.implementation;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.api.dto.AuthenticationRequest;
import org.ogorodnik.shop.api.dto.AuthenticationResponse;
import org.ogorodnik.shop.api.dto.RegisterRequest;
import org.ogorodnik.shop.security.utils.JwtUtils;
import org.ogorodnik.shop.security.entity.Credentials;
import org.ogorodnik.shop.repository.UserRepository;
import org.ogorodnik.shop.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAuthenticationService implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //TODO: do not know how to make role enum case insensitive
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        Credentials credentials = Credentials.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .role(request.getRole())
                .build();
        userRepository.save(credentials);

        String jwtToken = jwtUtils.generateToken(credentials);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));
        Credentials credentials = userRepository.findByUsernameIgnoreCase(request.getUsername())
                .orElseThrow();
        String jwtToken = jwtUtils.generateToken(credentials);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}

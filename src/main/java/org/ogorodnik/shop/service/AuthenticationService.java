package org.ogorodnik.shop.service;

import org.ogorodnik.shop.api.dto.AuthenticationRequest;
import org.ogorodnik.shop.api.dto.AuthenticationResponse;
import org.ogorodnik.shop.api.dto.RegisterRequest;

public interface AuthenticationService {
    //TODO: do not know how to make role enum case insensitive
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}

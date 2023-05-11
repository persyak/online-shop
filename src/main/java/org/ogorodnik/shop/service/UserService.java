package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.ogorodnik.shop.repository.UserRepository;
import org.ogorodnik.shop.entity.Credentials;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Credentials getCredentials(String login) throws AuthenticationException {
        Optional<Credentials> credentials = userRepository.findByLoginIgnoreCase(login);

        if (credentials.isEmpty()) {
            throw new AuthenticationException("User does not exist");
        }

        return credentials.get();
    }
}

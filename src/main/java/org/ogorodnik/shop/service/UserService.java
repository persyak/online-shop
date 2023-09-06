package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.repository.UserRepository;
import org.ogorodnik.shop.security.entity.Credentials;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Credentials> credentials = userRepository.findByUsernameIgnoreCase(username);

        if (credentials.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist");
        }
        return credentials.get();
    }
}

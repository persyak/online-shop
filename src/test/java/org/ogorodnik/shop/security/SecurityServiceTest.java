package org.ogorodnik.shop.security;

import org.junit.jupiter.api.Test;
import org.ogorodnik.shop.service.UserService;

import static org.mockito.Mockito.mock;

class SecurityServiceTest {

    @Test
    public void getSession() {
        String testUserToken = "XXXYYY";
        UserService mockUserService = mock(UserService.class);
        SecurityService testSecurityService = new SecurityService(mockUserService);
        assert (testSecurityService.getSession(testUserToken).isEmpty());
    }
}
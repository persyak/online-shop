package org.ogorodnik.shop.web.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebUtilTest {
    String testCookieName;
    HttpServletRequest testHttpServlet;

    @BeforeEach
    public void before() {
        testHttpServlet = mock(HttpServletRequest.class);
        testCookieName = "user-token";
    }

    @Test
    public void testExtractCookieValueWhenCookieIsNull() {
        assertTrue(WebUtil.extractCookieValue(testHttpServlet, testCookieName).isEmpty());
    }

    @Test
    public void testExtractCookieValueWhenCookieIsNotNull() {
        String testCookieValue = "testCookieValue";

        Cookie cookie = new Cookie("user-token", testCookieValue);
        List<Cookie> cookies = new java.util.ArrayList<>(List.of());
        cookies.add(cookie);
        Cookie[] testCookies = cookies.toArray(new Cookie[0]);
        when(testHttpServlet.getCookies()).thenReturn(testCookies);

        Optional<String> actualCookieOptional = WebUtil.extractCookieValue(testHttpServlet, testCookieName);
        assertTrue(actualCookieOptional.isPresent());
        assert (actualCookieOptional.get().equals(testCookieValue));
    }

    @AfterEach
    public void after() {
        testHttpServlet = null;
        testCookieName = null;
    }
}
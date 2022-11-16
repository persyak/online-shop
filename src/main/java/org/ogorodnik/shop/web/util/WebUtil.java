package org.ogorodnik.shop.web.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.stream.Stream;

public class WebUtil {

    //TODO: write a test
    public static Optional<String> extractCookieValue(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if (cookies == null){
            return Optional.empty();
        }

        return Stream.of(cookies)
                .filter(cookie -> cookie.getName().equalsIgnoreCase(cookieName))
                .map(Cookie::getValue)
                .findAny();
    }
}

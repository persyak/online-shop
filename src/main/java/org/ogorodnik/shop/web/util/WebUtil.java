package org.ogorodnik.shop.web.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.stream.Stream;

public class WebUtil {

    public static Optional<String> extractCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        String userToken = request.getParameter("userToken");
        if (null != cookies) {
            return Stream.of(cookies)
                    .filter(cookie -> cookie.getName().equalsIgnoreCase(cookieName))
                    .map(Cookie::getValue)
                    .findAny();
        } else if (null != userToken) {
            return Optional.of(userToken);
        }
        return Optional.empty();
    }
}

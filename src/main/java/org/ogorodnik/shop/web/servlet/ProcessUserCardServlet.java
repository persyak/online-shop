package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Session;
import org.ogorodnik.shop.service.SecurityService;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Slf4j
public class ProcessUserCardServlet extends HttpServlet {

    private SecurityService securityService;
    private PageGenerator pageGenerator;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uuid = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("user-token".equals(cookie.getName())) {
                uuid = cookie.getValue();
                break;
            }
        }
        if (null != uuid) {
            log.info("got user session. processing user card");
            Session session = securityService.getSession(uuid);
            List<Long> card = session.getCard();
            if (card.size() > 0) {
                Map<String, Object> paramsMap = new HashMap<>();
                try {
                    paramsMap.put("items", securityService.getCard(card));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String page = pageGenerator.getPage("usercard.html", paramsMap);
                response.getWriter().write(page);
            } else {
                String page = pageGenerator.getPage("usercardisempty.html");
                response.getWriter().write(page);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String stringId = uri.substring(10);
        long id = Long.parseLong(stringId);

        String uuid = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("user-token".equals(cookie.getName())) {
                uuid = cookie.getValue();
                break;
            }
        }
        Session session = securityService.getSession(uuid);
        session.addItemToTheCard(id);
        log.info("item with id " + id + " has been added to the card");
        response.sendRedirect("/items");
    }
}

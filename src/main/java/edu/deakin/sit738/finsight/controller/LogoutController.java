package edu.deakin.sit738.finsight.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.deakin.sit738.finsight.util.AppLogger;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logoutGet() {
        return "redirect:/dashboard";
    }

    @PostMapping("/logout")
    public String logoutPost(
            HttpServletRequest request,
            HttpServletResponse response) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler()
                    .logout(request, response, authentication);
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath(request.getContextPath().isEmpty()
                ? "/"
                : request.getContextPath());
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        AppLogger.info("User logged out.");
        return "redirect:/login";
    }
}

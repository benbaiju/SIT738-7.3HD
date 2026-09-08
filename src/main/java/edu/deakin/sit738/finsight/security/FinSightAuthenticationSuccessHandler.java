package edu.deakin.sit738.finsight.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.service.UserService;
import edu.deakin.sit738.finsight.util.AppLogger;
import edu.deakin.sit738.finsight.util.CsrfTokenUtil;
import edu.deakin.sit738.finsight.util.SessionAuthUtil;

@Component
public class FinSightAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        String email = authentication.getName();
        User user = userService.findByEmail(email);

        HttpSession session = request.getSession(true);

        if (user != null) {
            user.setPassword(null);
            session.setAttribute(SessionAuthUtil.SESSION_USER_ATTRIBUTE, user);
            CsrfTokenUtil.createToken(session);
            AppLogger.info("Successful login. userId=" + user.getId()
                    + " role=" + UserRoles.normalize(user.getRole()));
        }

        String redirectPath = "/dashboard";

        if (user != null) {
            if (UserRoles.isAdmin(user.getRole())) {
                redirectPath = "/admin/users";
            } else if (UserRoles.isAdvisor(user.getRole())) {
                redirectPath = "/advisor/clients";
            }
        }

        response.sendRedirect(request.getContextPath() + redirectPath);
    }
}

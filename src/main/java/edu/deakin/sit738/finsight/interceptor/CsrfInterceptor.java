package edu.deakin.sit738.finsight.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import edu.deakin.sit738.finsight.util.CsrfTokenUtil;

public class CsrfInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getServletPath();
        if (path == null || path.isEmpty()) {
            path = request.getRequestURI().substring(
                    request.getContextPath().length());
        }

        if ("/login".equals(path)
                || "/register".equals(path)
                || "/expenses/search".equals(path)) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return false;
        }

        if (!isValidReferer(request)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Invalid or missing Referer header.");
            return false;
        }

        String submittedToken =
                request.getParameter(CsrfTokenUtil.REQUEST_PARAMETER);

        if (!CsrfTokenUtil.isValid(session, submittedToken)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Invalid or missing CSRF token.");
            return false;
        }

        return true;
    }

    private boolean isValidReferer(HttpServletRequest request) {
        String referer = request.getHeader("Referer");

        if (referer == null || referer.trim().isEmpty()) {
            return false;
        }

        StringBuilder expected = new StringBuilder();
        expected.append(request.getScheme())
                .append("://")
                .append(request.getServerName());

        int port = request.getServerPort();
        if (("http".equals(request.getScheme()) && port != 80)
                || ("https".equals(request.getScheme()) && port != 443)) {
            expected.append(":").append(port);
        }

        expected.append(request.getContextPath());

        return referer.startsWith(expected.toString());
    }
}

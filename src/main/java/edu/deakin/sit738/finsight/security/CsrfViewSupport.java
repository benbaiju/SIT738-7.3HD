package edu.deakin.sit738.finsight.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CsrfViewSupport {

    private final HttpSessionCsrfTokenRepository csrfTokenRepository =
            new HttpSessionCsrfTokenRepository();

    @ModelAttribute
    public void exposeSpringCsrfToken(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {

        CsrfToken token =
                (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (token == null) {
            token = csrfTokenRepository.loadToken(request);
        }

        if (token == null) {
            token = csrfTokenRepository.generateToken(request);
            csrfTokenRepository.saveToken(token, request, response);
            request.setAttribute(CsrfToken.class.getName(), token);
        }

        model.addAttribute("springCsrfParameterName", token.getParameterName());
        model.addAttribute("springCsrfToken", token.getToken());
    }
}

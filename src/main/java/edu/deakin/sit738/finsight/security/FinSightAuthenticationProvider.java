package edu.deakin.sit738.finsight.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.service.UserService;
import edu.deakin.sit738.finsight.util.AppLogger;

@Component
public class FinSightAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String email = authentication.getName();
        Object credentials = authentication.getCredentials();

        if (email == null || credentials == null) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String rawPassword = credentials.toString();
        User user = userService.findByEmail(email);

        if (user == null || user.getPassword() == null) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String storedPassword = user.getPassword();
        boolean matches = false;

        if (isBcryptHash(storedPassword)) {
            matches = passwordEncoder.matches(rawPassword, storedPassword);
        } else if (storedPassword.equals(rawPassword)) {
            matches = true;
            user.setPassword(passwordEncoder.encode(rawPassword));
            userService.save(user);
            AppLogger.info("Upgraded plaintext password to BCrypt. userId="
                    + user.getId());
        }

        if (!matches) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String authority = UserRoles.toAuthority(user.getRole());

        return new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(authority)));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }

    private boolean isBcryptHash(String value) {
        return value.startsWith("$2a$")
                || value.startsWith("$2b$")
                || value.startsWith("$2y$");
    }
}

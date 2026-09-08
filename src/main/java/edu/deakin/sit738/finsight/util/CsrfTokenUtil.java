package edu.deakin.sit738.finsight.util;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.http.HttpSession;

public final class CsrfTokenUtil {

    public static final String SESSION_ATTRIBUTE = "csrfToken";
    public static final String REQUEST_PARAMETER = "csrfToken";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private CsrfTokenUtil() {
    }

    public static String createToken(HttpSession session) {
        String token = new BigInteger(130, SECURE_RANDOM).toString(32);
        session.setAttribute(SESSION_ATTRIBUTE, token);
        return token;
    }

    public static String getToken(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(SESSION_ATTRIBUTE);
        return value == null ? null : value.toString();
    }

    public static boolean isValid(HttpSession session, String submittedToken) {
        String sessionToken = getToken(session);
        if (sessionToken == null || submittedToken == null
                || submittedToken.isEmpty()) {
            return false;
        }
        return sessionToken.equals(submittedToken);
    }
}

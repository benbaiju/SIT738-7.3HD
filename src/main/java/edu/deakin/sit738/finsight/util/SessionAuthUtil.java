package edu.deakin.sit738.finsight.util;

import javax.servlet.http.HttpSession;

import edu.deakin.sit738.finsight.entity.User;

public final class SessionAuthUtil {

    public static final String SESSION_USER_ATTRIBUTE = "loggedInUser";

    private SessionAuthUtil() {
    }

    public static User getLoggedInUser(HttpSession session) {
        if (session == null) {
            return null;
        }

        Object value = session.getAttribute(SESSION_USER_ATTRIBUTE);
        if (value instanceof User) {
            return (User) value;
        }

        return null;
    }
}

package edu.deakin.sit738.finsight.security;

public final class UserRoles {

    public static final String USER = "USER";
    public static final String ADVISOR = "ADVISOR";
    public static final String ADMIN = "ADMIN";

    private UserRoles() {
    }

    public static String normalize(String role) {
        if (role == null || role.trim().isEmpty()) {
            return USER;
        }

        String normalised = role.trim().toUpperCase();

        if (ADVISOR.equals(normalised)
                || ADMIN.equals(normalised)
                || USER.equals(normalised)) {
            return normalised;
        }

        return USER;
    }

    public static String toAuthority(String role) {
        return "ROLE_" + normalize(role);
    }

    public static boolean isAdvisor(String role) {
        return ADVISOR.equals(normalize(role));
    }

    public static boolean isAdmin(String role) {
        return ADMIN.equals(normalize(role));
    }

    public static boolean isUser(String role) {
        return USER.equals(normalize(role));
    }
}

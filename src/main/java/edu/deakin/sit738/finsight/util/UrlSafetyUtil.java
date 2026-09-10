package edu.deakin.sit738.finsight.util;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class UrlSafetyUtil {

    private static final Set<String> ALLOWED_HOSTS =
            Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
                    "example.com",
                    "www.example.com",
                    "raw.githubusercontent.com",
                    "gist.githubusercontent.com"
            )));

    private UrlSafetyUtil() {
    }

    public static void validateHttpsUrl(String rawUrl) throws Exception {
        if (rawUrl == null || rawUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("URL is required.");
        }

        String trimmed = rawUrl.trim();
        URI uri = new URI(trimmed);

        if (uri.getScheme() == null
                || !"https".equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalArgumentException("Only HTTPS URLs are allowed.");
        }

        if (uri.getHost() == null || uri.getHost().trim().isEmpty()) {
            throw new IllegalArgumentException("URL host is required.");
        }

        if (uri.getUserInfo() != null) {
            throw new IllegalArgumentException("URL user information is not allowed.");
        }

        String host = uri.getHost().toLowerCase(Locale.ROOT);

        if (isBlockedHostname(host)) {
            throw new IllegalArgumentException("Internal or metadata hosts are not allowed.");
        }

        if (!isAllowlistedHost(host)) {
            throw new IllegalArgumentException(
                    "The requested domain is not on the approved allowlist.");
        }

        validateResolvedAddresses(host);
    }

    public static boolean isAllowlistedHost(String host) {
        if (host == null || host.trim().isEmpty()) {
            return false;
        }

        String normalised = host.toLowerCase(Locale.ROOT).trim();

        for (String allowed : ALLOWED_HOSTS) {
            if (normalised.equals(allowed)
                    || normalised.endsWith("." + allowed)) {
                return true;
            }
        }

        return false;
    }

    public static Set<String> getAllowedHosts() {
        return ALLOWED_HOSTS;
    }

    public static void validateResolvedAddresses(String host) throws UnknownHostException {
        InetAddress[] addresses = InetAddress.getAllByName(host);

        if (addresses == null || addresses.length == 0) {
            throw new IllegalArgumentException("Unable to resolve the destination host.");
        }

        for (InetAddress address : addresses) {
            if (isBlockedAddress(address)) {
                throw new IllegalArgumentException(
                        "Internal or private network destinations are not allowed.");
            }
        }
    }

    public static boolean isBlockedHostname(String host) {
        if (host == null) {
            return true;
        }

        String normalised = host.toLowerCase(Locale.ROOT).trim();

        if ("localhost".equals(normalised)
                || normalised.endsWith(".localhost")
                || normalised.endsWith(".local")
                || normalised.endsWith(".internal")
                || "metadata.google.internal".equals(normalised)
                || "metadata".equals(normalised)
                || "0.0.0.0".equals(normalised)
                || "[::1]".equals(normalised)
                || "::1".equals(normalised)
                || "169.254.169.254".equals(normalised)
                || "metadata.azure.com".equals(normalised)) {
            return true;
        }

        return false;
    }

    public static boolean isBlockedAddress(InetAddress address) {
        if (address == null) {
            return true;
        }

        if (address.isAnyLocalAddress()
                || address.isLoopbackAddress()
                || address.isLinkLocalAddress()
                || address.isSiteLocalAddress()
                || address.isMulticastAddress()) {
            return true;
        }

        byte[] bytes = address.getAddress();

        if (bytes.length == 4) {
            int first = bytes[0] & 0xFF;
            int second = bytes[1] & 0xFF;

            if (first == 10) {
                return true;
            }
            if (first == 172 && second >= 16 && second <= 31) {
                return true;
            }
            if (first == 192 && second == 168) {
                return true;
            }
            if (first == 127) {
                return true;
            }
            if (first == 169 && second == 254) {
                return true;
            }
            if (first == 100 && second >= 64 && second <= 127) {
                return true;
            }
            if (first == 0) {
                return true;
            }
        }

        if (bytes.length == 16) {
            if ((bytes[0] & 0xFE) == 0xFC) {
                return true;
            }
            if (bytes[0] == 0x20 && bytes[1] == 0x01
                    && bytes[2] == 0x00 && bytes[3] == 0x00) {
                return true;
            }
        }

        return false;
    }
}

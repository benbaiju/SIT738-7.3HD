package edu.deakin.sit738.finsight.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.servlet.http.HttpServletRequest;

public final class AppLogger {

    private static final Logger LOGGER = Logger.getLogger("finsight");
    private static boolean configured = false;

    private AppLogger() {
    }

    private static synchronized void ensureConfigured() {
        if (configured) {
            return;
        }

        try {
            File logDirectory = new File("logs");
            if (!logDirectory.exists()) {
                logDirectory.mkdirs();
            }

            FileHandler fileHandler =
                    new FileHandler("logs/finsight.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setUseParentHandlers(false);
            LOGGER.setLevel(Level.INFO);
        } catch (IOException e) {
            LOGGER.setUseParentHandlers(true);
            LOGGER.log(Level.SEVERE,
                    "Unable to initialise FinSight file logging", e);
        }

        configured = true;
    }

    public static void info(String message) {
        ensureConfigured();
        LOGGER.info(message);
    }

    public static void warn(String message) {
        ensureConfigured();
        LOGGER.warning(message);
    }

    public static void error(String message) {
        ensureConfigured();
        LOGGER.severe(message);
    }

    public static void error(String message, Throwable throwable) {
        ensureConfigured();
        LOGGER.log(Level.SEVERE, message, throwable);
    }

    public static String requestContext(HttpServletRequest request) {
        if (request == null) {
            return "method=unknown path=unknown";
        }

        return "method=" + request.getMethod()
                + " path=" + request.getRequestURI();
    }
}

package edu.deakin.sit738.finsight.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import edu.deakin.sit738.finsight.util.AppLogger;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request,
            Model model) {

        AppLogger.warn("Invalid request parameter. "
                + AppLogger.requestContext(request)
                + " exceptionType=" + ex.getClass().getSimpleName());

        model.addAttribute("errorTitle", "Invalid request");
        model.addAttribute("errorMessage",
                "The request contained an invalid value. Please try again.");
        return "error";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMaxUploadSize(
            MaxUploadSizeExceededException ex,
            HttpServletRequest request,
            Model model) {

        AppLogger.warn("Upload size exceeded. "
                + AppLogger.requestContext(request));

        model.addAttribute("errorTitle", "Upload rejected");
        model.addAttribute("errorMessage",
                "The uploaded file exceeds the maximum allowed size.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUnexpected(
            Exception ex,
            HttpServletRequest request,
            Model model) {

        AppLogger.error("Unexpected application error. "
                + AppLogger.requestContext(request)
                + " exceptionType=" + ex.getClass().getName(), ex);

        model.addAttribute("errorTitle", "Something went wrong");
        model.addAttribute("errorMessage",
                "An unexpected error occurred. Please try again later.");
        return "error";
    }
}

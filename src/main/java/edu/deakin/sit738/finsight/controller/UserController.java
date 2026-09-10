package edu.deakin.sit738.finsight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.security.UserRoles;
import edu.deakin.sit738.finsight.service.UserService;
import edu.deakin.sit738.finsight.util.AppLogger;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {

        User existingUser = userService.findByEmail(email);

        if (existingUser != null) { 
            model.addAttribute("error", "Email is already registered.");
            return "register";
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(fullName, email, hashedPassword);
        user.setRole(UserRoles.USER);
        user.setAdvisorId(null);
        userService.save(user);

        AppLogger.info("New user registered with role USER.");
        model.addAttribute("message", "Registration successful. Please login.");
        return "login";
    }

    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(value = "error", required = false) String error,
            Model model) {

        if (error != null) {
            AppLogger.warn("Failed login attempt.");
            model.addAttribute("error", "Invalid email or password.");
        }

        return "login";
    }
}

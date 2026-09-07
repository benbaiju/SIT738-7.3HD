package edu.deakin.sit738.finsight.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

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

        User user = new User(fullName, email, password);
        userService.save(user);

        model.addAttribute("message", "Registration successful. Please login.");
        return "login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {

        User user = userService.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {


            session.setAttribute("loggedInUser", user);

            model.addAttribute("user", user);
            model.addAttribute("userId", user.getId());

            return "dashboard";
        }

        model.addAttribute("error", "Invalid email or password.");
        return "login";
    }
}
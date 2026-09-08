package edu.deakin.sit738.finsight.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.security.UserRoles;
import edu.deakin.sit738.finsight.service.UserService;
import edu.deakin.sit738.finsight.util.AppLogger;
import edu.deakin.sit738.finsight.util.SessionAuthUtil;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/users")
    public String showUsers(HttpSession session, Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null || !UserRoles.isAdmin(loggedInUser.getRole())) {
            AppLogger.warn("Unauthorized admin access attempt.");
            return "redirect:/login";
        }

        List<User> users = userService.findAllUsers();
        for (User user : users) {
            user.setPassword(null);
        }

        List<User> advisors = userService.findAdvisors();
        for (User advisor : advisors) {
            advisor.setPassword(null);
        }

        model.addAttribute("adminId", loggedInUser.getId());
        model.addAttribute("users", users);
        model.addAttribute("advisors", advisors);

        return "admin-users";
    }

    @PostMapping("/admin/users/role")
    public String updateRole(
            @RequestParam("userId") int userId,
            @RequestParam("role") String role,
            HttpSession session) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null || !UserRoles.isAdmin(loggedInUser.getRole())) {
            AppLogger.warn("Unauthorized admin role update attempt.");
            return "redirect:/login";
        }

        try {
            userService.updateUserRole(userId, role);
            AppLogger.info("Admin updated user role. adminId="
                    + loggedInUser.getId() + " userId=" + userId
                    + " role=" + UserRoles.normalize(role));
        } catch (IllegalArgumentException ex) {
            AppLogger.warn("Admin role update failed. userId=" + userId);
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/assign-advisor")
    public String assignAdvisor(
            @RequestParam("userId") int userId,
            @RequestParam(value = "advisorId", required = false) Integer advisorId,
            HttpSession session) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null || !UserRoles.isAdmin(loggedInUser.getRole())) {
            AppLogger.warn("Unauthorized admin advisor assign attempt.");
            return "redirect:/login";
        }

        try {
            Integer selectedId = advisorId;
            if (selectedId != null && selectedId.intValue() <= 0) {
                selectedId = null;
            }

            userService.assignAdvisorToClient(userId, selectedId);
            AppLogger.info("Admin assigned advisor. adminId="
                    + loggedInUser.getId() + " clientId=" + userId
                    + " advisorId=" + selectedId);
        } catch (IllegalArgumentException ex) {
            AppLogger.warn("Admin advisor assign failed. clientId=" + userId);
        }

        return "redirect:/admin/users";
    }
}

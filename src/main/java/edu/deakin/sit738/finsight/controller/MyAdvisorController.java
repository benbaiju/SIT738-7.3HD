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
public class MyAdvisorController {

    @Autowired
    private UserService userService;

    @GetMapping("/my-advisor")
    public String showMyAdvisor(HttpSession session, Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null || !UserRoles.isUser(loggedInUser.getRole())) {
            AppLogger.warn("Unauthorized my-advisor access attempt.");
            return "redirect:/login";
        }

        User client = userService.findById(loggedInUser.getId());
        if (client == null) {
            return "redirect:/login";
        }

        client.setPassword(null);
        session.setAttribute(SessionAuthUtil.SESSION_USER_ATTRIBUTE, client);

        User currentAdvisor = null;
        if (client.getAdvisorId() != null) {
            currentAdvisor = userService.findById(client.getAdvisorId());
            if (currentAdvisor != null) {
                currentAdvisor.setPassword(null);
            }
        }

        List<User> advisors = userService.findAdvisors();
        for (User advisor : advisors) {
            advisor.setPassword(null);
        }

        model.addAttribute("userId", client.getId());
        model.addAttribute("client", client);
        model.addAttribute("currentAdvisor", currentAdvisor);
        model.addAttribute("advisors", advisors);

        return "my-advisor";
    }

    @PostMapping("/my-advisor/select")
    public String selectAdvisor(
            @RequestParam(value = "advisorId", required = false) Integer advisorId,
            HttpSession session,
            Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null || !UserRoles.isUser(loggedInUser.getRole())) {
            AppLogger.warn("Unauthorized advisor selection attempt.");
            return "redirect:/login";
        }

        try {
            Integer selectedId = advisorId;
            if (selectedId != null && selectedId.intValue() <= 0) {
                selectedId = null;
            }

            userService.assignAdvisorToClient(loggedInUser.getId(), selectedId);

            User refreshed = userService.findById(loggedInUser.getId());
            if (refreshed != null) {
                refreshed.setPassword(null);
                session.setAttribute(
                        SessionAuthUtil.SESSION_USER_ATTRIBUTE, refreshed);
            }

            AppLogger.info("Client updated advisor assignment. userId="
                    + loggedInUser.getId());
        } catch (IllegalArgumentException ex) {
            AppLogger.warn("Invalid advisor selection. userId="
                    + loggedInUser.getId());
            model.addAttribute("error", ex.getMessage());
            return showMyAdvisor(session, model);
        }

        return "redirect:/my-advisor";
    }
}

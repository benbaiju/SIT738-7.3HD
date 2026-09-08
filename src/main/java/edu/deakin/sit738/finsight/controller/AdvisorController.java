package edu.deakin.sit738.finsight.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.security.UserRoles;
import edu.deakin.sit738.finsight.service.ExpenseService;
import edu.deakin.sit738.finsight.service.UserService;
import edu.deakin.sit738.finsight.util.AppLogger;
import edu.deakin.sit738.finsight.util.SessionAuthUtil;

@Controller
public class AdvisorController {

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/advisor/clients")
    public String showClients(HttpSession session, Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null
                || !UserRoles.isAdvisor(loggedInUser.getRole())) {
            AppLogger.warn("Unauthorized advisor clients access attempt.");
            return "redirect:/login";
        }

        List<User> clients =
                userService.findClientsByAdvisorId(loggedInUser.getId());

        for (User client : clients) {
            client.setPassword(null);
        }

        model.addAttribute("advisorId", loggedInUser.getId());
        model.addAttribute("clients", clients);

        return "advisor-clients";
    }

    @GetMapping("/advisor/client-insights")
    public String showClientInsights(
            @RequestParam("clientId") int clientId,
            HttpSession session,
            Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null
                || !UserRoles.isAdvisor(loggedInUser.getRole())) {
            AppLogger.warn("Unauthorized advisor insights access attempt.");
            return "redirect:/login";
        }

        if (!userService.isAssignedClient(loggedInUser.getId(), clientId)) {
            AppLogger.warn("Blocked advisor access to unassigned client. "
                    + "advisorId=" + loggedInUser.getId()
                    + " clientId=" + clientId);
            model.addAttribute("error",
                    "You are not assigned to this client.");
            return showClients(session, model);
        }

        User client = userService.findById(clientId);
        if (client != null) {
            client.setPassword(null);
        }

        Map<String, Object> insights =
                expenseService.getFinancialInsights(clientId);

        model.addAttribute("advisorId", loggedInUser.getId());
        model.addAttribute("client", client);
        model.addAttribute("insights", insights);

        return "advisor-client-insights";
    }
}

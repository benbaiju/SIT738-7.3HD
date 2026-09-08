package edu.deakin.sit738.finsight.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.FinancialGoal;
import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.service.FinancialGoalService;
import edu.deakin.sit738.finsight.util.AppLogger;
import edu.deakin.sit738.finsight.util.SessionAuthUtil;

@Controller
public class FinancialGoalController {

    @Autowired
    private FinancialGoalService financialGoalService;

    @GetMapping("/goals")
    public String showGoals(HttpSession session, Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized goals access attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        List<FinancialGoal> goals =
                financialGoalService.getGoalsByUserId(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("goals", goals);

        return "goals";
    }

    @PostMapping("/goals/add")
    public String addGoal(
            @RequestParam("goalName") String goalName,
            @RequestParam("description") String description,
            @RequestParam("targetAmount") double targetAmount,
            @RequestParam("currentAmount") double currentAmount,
            HttpSession session) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized goal add attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        FinancialGoal goal = new FinancialGoal();
        goal.setUserId(userId);
        goal.setGoalName(goalName);
        goal.setDescription(description);
        goal.setTargetAmount(targetAmount);
        goal.setCurrentAmount(currentAmount);

        financialGoalService.saveGoal(goal);

        return "redirect:/goals?userId=" + userId;
    }

    @GetMapping("/goals/edit")
    public String showEditGoal(
            @RequestParam("id") int id,
            HttpSession session,
            Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized goal edit access attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();
        FinancialGoal goal =
                financialGoalService.getGoalByIdForUser(id, userId);

        if (goal == null) {
            AppLogger.warn("Blocked goal edit for non-owned record. userId="
                    + userId + " goalId=" + id);
            return "redirect:/goals?userId=" + userId;
        }

        model.addAttribute("userId", userId);
        model.addAttribute("goal", goal);

        return "edit-goal";
    }

    @PostMapping("/goals/update")
    public String updateGoal(
            @RequestParam("id") int id,
            @RequestParam("goalName") String goalName,
            @RequestParam("description") String description,
            @RequestParam("targetAmount") double targetAmount,
            @RequestParam("currentAmount") double currentAmount,
            HttpSession session) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized goal update attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();
        FinancialGoal goal =
                financialGoalService.getGoalByIdForUser(id, userId);

        if (goal == null) {
            AppLogger.warn("Blocked goal update for non-owned record. userId="
                    + userId + " goalId=" + id);
            return "redirect:/goals?userId=" + userId;
        }

        goal.setGoalName(goalName);
        goal.setDescription(description);
        goal.setTargetAmount(targetAmount);
        goal.setCurrentAmount(currentAmount);

        financialGoalService.saveGoal(goal);

        return "redirect:/goals?userId=" + userId;
    }

    @PostMapping("/goals/delete")
    public String deleteGoal(
            @RequestParam("id") int id,
            HttpSession session) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized goal delete attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();
        financialGoalService.deleteGoal(id, userId);

        return "redirect:/goals?userId=" + userId;
    }
}

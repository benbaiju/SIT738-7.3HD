package edu.deakin.sit738.finsight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.FinancialGoal;
import edu.deakin.sit738.finsight.service.FinancialGoalService;

@Controller
public class FinancialGoalController {

    @Autowired
    private FinancialGoalService financialGoalService;

    @GetMapping("/goals")
    public String showGoals(
            @RequestParam("userId") int userId,
            Model model) {

        List<FinancialGoal> goals =
                financialGoalService.getGoalsByUserId(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("goals", goals);

        return "goals";
    }

    @PostMapping("/goals/add")
    public String addGoal(
            @RequestParam("userId") int userId,
            @RequestParam("goalName") String goalName,
            @RequestParam("description") String description,
            @RequestParam("targetAmount") double targetAmount,
            @RequestParam("currentAmount") double currentAmount) {

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
            @RequestParam("userId") int userId,
            Model model) {

        FinancialGoal goal = financialGoalService.getGoalById(id);

        model.addAttribute("userId", userId);
        model.addAttribute("goal", goal);

        return "edit-goal";
    }

    @PostMapping("/goals/update")
    public String updateGoal(
            @RequestParam("id") int id,
            @RequestParam("userId") int userId,
            @RequestParam("goalName") String goalName,
            @RequestParam("description") String description,
            @RequestParam("targetAmount") double targetAmount,
            @RequestParam("currentAmount") double currentAmount) {

        FinancialGoal goal = financialGoalService.getGoalById(id);

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
            @RequestParam("userId") int userId) {

        financialGoalService.deleteGoal(id);

        return "redirect:/goals?userId=" + userId;
    }
}
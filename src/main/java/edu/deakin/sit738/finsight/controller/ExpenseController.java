package edu.deakin.sit738.finsight.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.Expense;
import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.service.ExpenseService;

@Controller
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/expenses")
    public String showExpenses(
            @RequestParam("userId") int userId,
            Model model) {

        List<Expense> expenses =
                expenseService.getExpensesByUserId(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("expenses", expenses);
        model.addAttribute("expense", new Expense());

        return "expenses";
    }

    /**
     * Secure add expense endpoint (default).
     * Uses Bean Validation (@Valid) before persisting user input.
     */
    @PostMapping("/expenses/add")
    public String addExpense(
            @Valid @ModelAttribute("expense") Expense expense,
            BindingResult bindingResult,
            HttpSession session,
            Model model) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", userId);
            model.addAttribute("expenses",
                    expenseService.getExpensesByUserId(userId));
            model.addAttribute("validationErrors",
                    bindingResult.getAllErrors());
            return "expenses";
        }

        expense.setUserId(userId);
        expenseService.saveExpense(expense);

        return "redirect:/expenses?userId=" + userId;
    }

    /**
     * VULNERABLE (intentionally retained for SAST/DAST comparison):
     * Accepts and stores raw user input with no Bean Validation.
     * Do not expose this endpoint from the normal expenses UI.
     */
    @PostMapping("/expenses/add-vulnerable")
    public String addExpenseVulnerable(
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("amount") double amount,
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        Expense expense = new Expense();
        expense.setUserId(userId);
        expense.setDescription(description);
        expense.setCategory(category);
        expense.setAmount(amount);

        expenseService.saveExpense(expense);

        return "redirect:/expenses?userId=" + userId;
    }

    @PostMapping("/expenses/delete")
    public String deleteExpense(
            @RequestParam("id") int id,
            @RequestParam("userId") int userId) {

        expenseService.deleteExpense(id);

        return "redirect:/expenses?userId=" + userId;
    }

    @PostMapping("/expenses/search")
    public String searchExpenses(
            @RequestParam("description") String description,
            HttpSession session,
            Model model) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        List<Expense> expenses =
                expenseService.searchByDescription(userId, description);

        model.addAttribute("userId", userId);
        model.addAttribute("expenses", expenses);
        model.addAttribute("expense", new Expense());

        return "expenses";
    }

    @GetMapping("/financial-insights")
    public String financialInsights(
            @RequestParam("userId") int userId,
            Model model) {

        model.addAttribute(
                "insights",
                expenseService.getFinancialInsights(userId));

        model.addAttribute("userId", userId);

        return "financial-insights";
    }

}

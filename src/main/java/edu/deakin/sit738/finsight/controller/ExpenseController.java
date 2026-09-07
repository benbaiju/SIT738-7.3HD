package edu.deakin.sit738.finsight.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

        return "expenses";
    }

    @PostMapping("/expenses/add")
    public String addExpense(
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("amount") double amount,
            HttpSession session) {

        /*
         * Retrieve the authenticated user from the server-side session.
         * The userId is no longer accepted from the request.
         */
        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        /*
         * Reject requests from users who are not logged in.
         */
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        int authenticatedUserId = loggedInUser.getId();

        Expense expense = new Expense();

        expense.setUserId(authenticatedUserId);
        expense.setDescription(description);
        expense.setCategory(category);
        expense.setAmount(amount);

        expenseService.saveExpense(expense);

        return "redirect:/expenses?userId=" + authenticatedUserId;
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
            @RequestParam("userId") int userId,
            @RequestParam("description") String description,
            Model model) {

        List<Expense> expenses =
                expenseService.searchByDescription(description);

        model.addAttribute("userId", userId);
        model.addAttribute("expenses", expenses);

        return "expenses";
    }
}
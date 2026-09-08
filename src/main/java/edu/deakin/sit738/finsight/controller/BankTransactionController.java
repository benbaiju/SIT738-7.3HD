package edu.deakin.sit738.finsight.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.BankTransaction;
import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.service.BankTransactionService;
import edu.deakin.sit738.finsight.util.AppLogger;
import edu.deakin.sit738.finsight.util.SessionAuthUtil;

@Controller
public class BankTransactionController {

    @Autowired
    private BankTransactionService bankTransactionService;

    @GetMapping("/transactions")
    public String showTransactions(HttpSession session, Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized transactions access attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        List<BankTransaction> transactions =
                bankTransactionService.getTransactionsByUserId(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("transactions", transactions);

        return "transactions";
    }

    @PostMapping("/transactions/add")
    public String addTransaction(
            @RequestParam("transactionReference") String transactionReference,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("amount") double amount,
            @RequestParam("transactionType") String transactionType,
            HttpSession session) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized transaction add attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        BankTransaction transaction = new BankTransaction();
        transaction.setUserId(userId);
        transaction.setTransactionReference(transactionReference);
        transaction.setDescription(description);
        transaction.setCategory(category);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);

        bankTransactionService.saveTransaction(transaction);

        return "redirect:/transactions?userId=" + userId;
    }

    @PostMapping("/transactions/delete")
    public String deleteTransaction(
            @RequestParam("id") int id,
            HttpSession session) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized transaction delete attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();
        bankTransactionService.deleteTransaction(id, userId);

        return "redirect:/transactions?userId=" + userId;
    }
}

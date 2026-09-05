package edu.deakin.sit738.finsight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.BankTransaction;
import edu.deakin.sit738.finsight.service.BankTransactionService;

@Controller
public class BankTransactionController {

    @Autowired
    private BankTransactionService bankTransactionService;

    @GetMapping("/transactions")
    public String showTransactions(
            @RequestParam("userId") int userId,
            Model model) {

        List<BankTransaction> transactions =
                bankTransactionService.getTransactionsByUserId(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("transactions", transactions);

        return "transactions";
    }

    @PostMapping("/transactions/add")
    public String addTransaction(
            @RequestParam("userId") int userId,
            @RequestParam("transactionReference") String transactionReference,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("amount") double amount,
            @RequestParam("transactionType") String transactionType) {

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
            @RequestParam("userId") int userId) {

        bankTransactionService.deleteTransaction(id);

        return "redirect:/transactions?userId=" + userId;
    }
}
package edu.deakin.sit738.finsight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.BankTransaction;
import edu.deakin.sit738.finsight.entity.Expense;
import edu.deakin.sit738.finsight.entity.FinancialGoal;
import edu.deakin.sit738.finsight.entity.Investment;
import edu.deakin.sit738.finsight.entity.Loan;
import edu.deakin.sit738.finsight.service.BankTransactionService;
import edu.deakin.sit738.finsight.service.ExpenseService;
import edu.deakin.sit738.finsight.service.FinancialGoalService;
import edu.deakin.sit738.finsight.service.InvestmentService;
import edu.deakin.sit738.finsight.service.LoanService;

@Controller
public class DashboardController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private InvestmentService investmentService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private FinancialGoalService financialGoalService;

    @Autowired
    private BankTransactionService bankTransactionService;

    @GetMapping("/dashboard")
    public String showDashboard(
            @RequestParam("userId") int userId,
            Model model) {

        List<Expense> expenses =
                expenseService.getExpensesByUserId(userId);

        List<Investment> investments =
                investmentService.getInvestmentsByUserId(userId);

        List<Loan> loans =
                loanService.getLoansByUserId(userId);

        List<FinancialGoal> goals =
                financialGoalService.getGoalsByUserId(userId);

        List<BankTransaction> transactions =
                bankTransactionService.getTransactionsByUserId(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("expenses", expenses);
        model.addAttribute("investments", investments);
        model.addAttribute("loans", loans);
        model.addAttribute("goals", goals);
        model.addAttribute("transactions", transactions);

        return "dashboard";
    }
}
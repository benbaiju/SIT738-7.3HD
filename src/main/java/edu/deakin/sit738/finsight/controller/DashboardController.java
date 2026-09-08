package edu.deakin.sit738.finsight.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.deakin.sit738.finsight.entity.Expense;
import edu.deakin.sit738.finsight.entity.FinancialGoal;
import edu.deakin.sit738.finsight.entity.Investment;
import edu.deakin.sit738.finsight.entity.Loan;
import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.service.ExpenseService;
import edu.deakin.sit738.finsight.service.FinancialGoalService;
import edu.deakin.sit738.finsight.service.InvestmentService;
import edu.deakin.sit738.finsight.service.LoanService;
import edu.deakin.sit738.finsight.util.AppLogger;
import edu.deakin.sit738.finsight.util.SessionAuthUtil;

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

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized dashboard access attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        List<Expense> expenses =
                expenseService.getExpensesByUserId(userId);

        List<Investment> investments =
                investmentService.getInvestmentsByUserId(userId);

        List<Loan> loans =
                loanService.getLoansByUserId(userId);

        List<FinancialGoal> goals =
                financialGoalService.getGoalsByUserId(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("expenses", expenses);
        model.addAttribute("investments", investments);
        model.addAttribute("loans", loans);
        model.addAttribute("goals", goals);

        return "dashboard";
    }
}

package edu.deakin.sit738.finsight.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.Loan;
import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.service.LoanService;
import edu.deakin.sit738.finsight.util.AppLogger;
import edu.deakin.sit738.finsight.util.SessionAuthUtil;

@Controller
public class LoanController {

    @Autowired
    private LoanService loanService;

    @GetMapping("/loans")
    public String showLoans(HttpSession session, Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized loans access attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        List<Loan> loans =
                loanService.getLoansByUserId(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("loans", loans);

        return "loans";
    }

    @PostMapping("/loans/add")
    public String addLoan(
            @RequestParam("loanType") String loanType,
            @RequestParam("lender") String lender,
            @RequestParam("principalAmount") double principalAmount,
            @RequestParam("outstandingBalance") double outstandingBalance,
            @RequestParam("interestRate") double interestRate,
            @RequestParam("monthlyRepayment") double monthlyRepayment,
            HttpSession session) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized loan add attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        Loan loan = new Loan();
        loan.setUserId(userId);
        loan.setLoanType(loanType);
        loan.setLender(lender);
        loan.setPrincipalAmount(principalAmount);
        loan.setOutstandingBalance(outstandingBalance);
        loan.setInterestRate(interestRate);
        loan.setMonthlyRepayment(monthlyRepayment);

        loanService.saveLoan(loan);

        return "redirect:/loans?userId=" + userId;
    }

    @PostMapping("/loans/delete")
    public String deleteLoan(
            @RequestParam("id") int id,
            HttpSession session) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized loan delete attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();
        loanService.deleteLoan(id, userId);

        return "redirect:/loans?userId=" + userId;
    }
}

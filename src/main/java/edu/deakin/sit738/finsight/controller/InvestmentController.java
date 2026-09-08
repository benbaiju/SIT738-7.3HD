package edu.deakin.sit738.finsight.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.Investment;
import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.service.InvestmentService;
import edu.deakin.sit738.finsight.util.AppLogger;
import edu.deakin.sit738.finsight.util.SessionAuthUtil;

@Controller
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

    @GetMapping("/investments")
    public String showInvestments(HttpSession session, Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized investments access attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        List<Investment> investments =
                investmentService.getInvestmentsByUserId(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("investments", investments);

        return "investments";
    }

    @PostMapping("/investments/add")
    public String addInvestment(
            @RequestParam("assetName") String assetName,
            @RequestParam("assetType") String assetType,
            @RequestParam("country") String country,
            @RequestParam("quantity") double quantity,
            @RequestParam("purchasePrice") double purchasePrice,
            @RequestParam("currentValue") double currentValue,
            HttpSession session) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized investment add attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        Investment investment = new Investment();
        investment.setUserId(userId);
        investment.setAssetName(assetName);
        investment.setAssetType(assetType);
        investment.setCountry(country);
        investment.setQuantity(quantity);
        investment.setPurchasePrice(purchasePrice);
        investment.setCurrentValue(currentValue);

        investmentService.saveInvestment(investment);

        return "redirect:/investments?userId=" + userId;
    }

    @PostMapping("/investments/delete")
    public String deleteInvestment(
            @RequestParam("id") int id,
            HttpSession session) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized investment delete attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();
        investmentService.deleteInvestment(id, userId);

        return "redirect:/investments?userId=" + userId;
    }
}

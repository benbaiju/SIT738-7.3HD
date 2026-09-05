package edu.deakin.sit738.finsight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.Investment;
import edu.deakin.sit738.finsight.service.InvestmentService;

@Controller
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

    @GetMapping("/investments")
    public String showInvestments(
            @RequestParam("userId") int userId,
            Model model) {

        List<Investment> investments =
                investmentService.getInvestmentsByUserId(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("investments", investments);

        return "investments";
    }

    @PostMapping("/investments/add")
    public String addInvestment(
            @RequestParam("userId") int userId,
            @RequestParam("assetName") String assetName,
            @RequestParam("assetType") String assetType,
            @RequestParam("country") String country,
            @RequestParam("quantity") double quantity,
            @RequestParam("purchasePrice") double purchasePrice,
            @RequestParam("currentValue") double currentValue) {

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
            @RequestParam("userId") int userId) {

        investmentService.deleteInvestment(id);

        return "redirect:/investments?userId=" + userId;
    }
}
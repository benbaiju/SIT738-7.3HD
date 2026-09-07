package edu.deakin.sit738.finsight.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.deakin.sit738.finsight.dao.ExpenseDAO;
import edu.deakin.sit738.finsight.entity.Expense;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseDAO expenseDAO;

    @Transactional
    public void saveExpense(Expense expense) {
        expenseDAO.save(expense);
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpensesByUserId(int userId) {
        return expenseDAO.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Expense> searchByDescription(String description) {
        return expenseDAO.searchByDescription(description);
    }

    @Transactional
    public void deleteExpense(int id) {
        expenseDAO.delete(id);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getFinancialInsights(int userId) {

        List<Expense> expenses =
                expenseDAO.findByUserId(userId);

        double totalExpenses = 0;
        double housingExpenses = 0;
        double foodExpenses = 0;
        double transportExpenses = 0;
        double loanExpenses = 0;
        double otherExpenses = 0;

        Map<String, Double> categoryTotals =
                new HashMap<String, Double>();

        if (expenses != null) {

            for (Expense expense : expenses) {

                double amount = expense.getAmount();
                String category = expense.getCategory();

                totalExpenses += amount;

                if (category == null) {
                    category = "Other";
                }

                String normalisedCategory =
                        category.trim().toLowerCase();

                if (normalisedCategory.contains("housing")
                        || normalisedCategory.contains("rent")) {

                    housingExpenses += amount;

                } else if (normalisedCategory.contains("food")
                        || normalisedCategory.contains("grocery")) {

                    foodExpenses += amount;

                } else if (normalisedCategory.contains("transport")
                        || normalisedCategory.contains("travel")) {

                    transportExpenses += amount;

                } else if (normalisedCategory.contains("loan")
                        || normalisedCategory.contains("debt")) {

                    loanExpenses += amount;

                } else {

                    otherExpenses += amount;
                }

                Double existingTotal =
                        categoryTotals.get(category);

                if (existingTotal == null) {
                    categoryTotals.put(category, amount);
                } else {
                    categoryTotals.put(
                            category,
                            existingTotal + amount);
                }
            }
        }

        String inferredInsight;

        if (totalExpenses == 0) {

            inferredInsight =
                    "There is not enough expense data to generate an insight.";

        } else if (loanExpenses > totalExpenses * 0.30) {

            inferredInsight =
                    "Loan-related expenses represent a significant "
                    + "portion of the recorded spending.";

        } else if (housingExpenses > totalExpenses * 0.40) {

            inferredInsight =
                    "Housing-related expenses represent a significant "
                    + "portion of the recorded spending.";

        } else if (foodExpenses > totalExpenses * 0.25) {

            inferredInsight =
                    "Food-related expenses represent a significant "
                    + "portion of the recorded spending.";

        } else {

            inferredInsight =
                    "The recorded expenses are distributed across "
                    + "several categories.";
        }

        Map<String, Object> insights =
                new HashMap<String, Object>();

        insights.put("totalExpenses", totalExpenses);
        insights.put("housingExpenses", housingExpenses);
        insights.put("foodExpenses", foodExpenses);
        insights.put("transportExpenses", transportExpenses);
        insights.put("loanExpenses", loanExpenses);
        insights.put("otherExpenses", otherExpenses);
        insights.put("categoryTotals", categoryTotals);
        insights.put("inferredInsight", inferredInsight);

        return insights;
    }
}
package edu.deakin.sit738.finsight.service;

import java.util.List;

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
}
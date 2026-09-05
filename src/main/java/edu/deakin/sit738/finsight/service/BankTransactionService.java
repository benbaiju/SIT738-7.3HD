package edu.deakin.sit738.finsight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.deakin.sit738.finsight.dao.BankTransactionDAO;
import edu.deakin.sit738.finsight.dao.ExpenseDAO;
import edu.deakin.sit738.finsight.entity.BankTransaction;
import edu.deakin.sit738.finsight.entity.Expense;

@Service
public class BankTransactionService {

    @Autowired
    private BankTransactionDAO bankTransactionDAO;

    @Autowired
    private ExpenseDAO expenseDAO;

    @Transactional
    public void saveTransaction(BankTransaction transaction) {
        bankTransactionDAO.save(transaction);

        if ("expense".equalsIgnoreCase(transaction.getTransactionType())) {
            Expense expense = new Expense(
                    transaction.getUserId(),
                    transaction.getDescription(),
                    transaction.getCategory(),
                    transaction.getAmount(),
                    transaction.getTransactionDate()
            );

            expenseDAO.save(expense);
        }
    }

    @Transactional(readOnly = true)
    public List<BankTransaction> getTransactionsByUserId(int userId) {
        return bankTransactionDAO.findByUserId(userId);
    }

    @Transactional
    public void deleteTransaction(int id) {
        bankTransactionDAO.delete(id);
    }
}
package edu.deakin.sit738.finsight.service;

import java.util.Date;
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

    public static String normaliseTransactionType(String transactionType) {

        if (transactionType == null || transactionType.trim().isEmpty()) {
            return "expense";
        }

        String normalisedType = transactionType.trim().toLowerCase();

        if (normalisedType.equals("income")
                || normalisedType.equals("credit")) {
            return "income";
        }

        if (normalisedType.equals("expense")
                || normalisedType.equals("debit")
                || normalisedType.equals("withdrawal")) {
            return "expense";
        }

        return "expense";
    }

    @Transactional
    public void saveTransaction(BankTransaction transaction) {

        transaction.setCategory(
                ExpenseService.normaliseCategory(transaction.getCategory()));
        transaction.setTransactionType(
                normaliseTransactionType(transaction.getTransactionType()));
        transaction.setAmount(Math.abs(transaction.getAmount()));

        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(new Date());
        }

        String reference = transaction.getTransactionReference();

        if (reference != null && !reference.trim().isEmpty()) {

            transaction.setTransactionReference(reference.trim());

            BankTransaction existing =
                    bankTransactionDAO.findByUserIdAndTransactionReference(
                            transaction.getUserId(),
                            transaction.getTransactionReference());

            if (existing != null) {
                return;
            }
        }

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
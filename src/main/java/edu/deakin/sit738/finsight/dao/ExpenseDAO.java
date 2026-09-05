package edu.deakin.sit738.finsight.dao;

import java.util.List;

import edu.deakin.sit738.finsight.entity.Expense;

public interface ExpenseDAO {

    void save(Expense expense);

    List<Expense> findByUserId(int userId);

    List<Expense> searchByDescription(String description);

    void delete(int id);
}
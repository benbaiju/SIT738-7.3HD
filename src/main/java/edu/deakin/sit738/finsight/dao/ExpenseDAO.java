package edu.deakin.sit738.finsight.dao;

import java.util.List;

import edu.deakin.sit738.finsight.entity.Expense;

public interface ExpenseDAO {

    void save(Expense expense);

    List<Expense> findByUserId(int userId);

    List<Expense> searchByDescription(int userId, String description);

    List<Expense> searchByDescriptionForUser(int userId, String description);

    Expense findById(int id);

    void delete(int id, int userId);
}

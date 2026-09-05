package edu.deakin.sit738.finsight.dao;

import java.util.List;
import edu.deakin.sit738.finsight.entity.FinancialGoal;

public interface FinancialGoalDAO {

    void save(FinancialGoal goal);

    List<FinancialGoal> findByUserId(int userId);

    FinancialGoal findById(int id);

    void delete(int id);
}
package edu.deakin.sit738.finsight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.deakin.sit738.finsight.dao.FinancialGoalDAO;
import edu.deakin.sit738.finsight.entity.FinancialGoal;

@Service
public class FinancialGoalService {

    @Autowired
    private FinancialGoalDAO financialGoalDAO;

    @Transactional
    public void saveGoal(FinancialGoal goal) {
        financialGoalDAO.save(goal);
    }

    @Transactional(readOnly = true)
    public List<FinancialGoal> getGoalsByUserId(int userId) {
        return financialGoalDAO.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public FinancialGoal getGoalByIdForUser(int id, int userId) {
        FinancialGoal goal = financialGoalDAO.findById(id);

        if (goal == null || goal.getUserId() != userId) {
            return null;
        }

        return goal;
    }

    @Transactional
    public void deleteGoal(int id, int userId) {
        financialGoalDAO.delete(id, userId);
    }
}

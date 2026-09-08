package edu.deakin.sit738.finsight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.deakin.sit738.finsight.dao.InvestmentDAO;
import edu.deakin.sit738.finsight.entity.Investment;

@Service
public class InvestmentService {

    @Autowired
    private InvestmentDAO investmentDAO;

    @Transactional
    public void saveInvestment(Investment investment) {
        investmentDAO.save(investment);
    }

    @Transactional(readOnly = true)
    public List<Investment> getInvestmentsByUserId(int userId) {
        return investmentDAO.findByUserId(userId);
    }

    @Transactional
    public void deleteInvestment(int id, int userId) {
        investmentDAO.delete(id, userId);
    }
}

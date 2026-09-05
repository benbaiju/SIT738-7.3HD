package edu.deakin.sit738.finsight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.deakin.sit738.finsight.dao.LoanDAO;
import edu.deakin.sit738.finsight.entity.Loan;

@Service
public class LoanService {

    @Autowired
    private LoanDAO loanDAO;

    @Transactional
    public void saveLoan(Loan loan) {
        loanDAO.save(loan);
    }

    @Transactional(readOnly = true)
    public List<Loan> getLoansByUserId(int userId) {
        return loanDAO.findByUserId(userId);
    }

    @Transactional
    public void deleteLoan(int id) {
        loanDAO.delete(id);
    }
}
package edu.deakin.sit738.finsight.dao;

import java.util.List;
import edu.deakin.sit738.finsight.entity.Loan;

public interface LoanDAO {

    void save(Loan loan);

    List<Loan> findByUserId(int userId);

    void delete(int id);
}
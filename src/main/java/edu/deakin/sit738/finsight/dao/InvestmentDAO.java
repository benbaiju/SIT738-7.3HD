package edu.deakin.sit738.finsight.dao;

import java.util.List;
import edu.deakin.sit738.finsight.entity.Investment;

public interface InvestmentDAO {

    void save(Investment investment);

    List<Investment> findByUserId(int userId);

    void delete(int id);
}
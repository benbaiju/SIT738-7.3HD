package edu.deakin.sit738.finsight.dao;

import java.util.List;
import edu.deakin.sit738.finsight.entity.BankTransaction;

public interface BankTransactionDAO {

    void save(BankTransaction transaction);

    List<BankTransaction> findByUserId(int userId);

    BankTransaction findByUserIdAndTransactionReference(
            int userId, String transactionReference);

    void delete(int id);
}
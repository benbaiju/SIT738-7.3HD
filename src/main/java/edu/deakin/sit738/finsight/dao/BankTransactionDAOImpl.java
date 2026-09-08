package edu.deakin.sit738.finsight.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.deakin.sit738.finsight.entity.BankTransaction;

@Repository
public class BankTransactionDAOImpl implements BankTransactionDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(BankTransaction transaction) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(transaction);
    }

    @Override
    public List<BankTransaction> findByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "from BankTransaction where userId = :userId";

        return session.createQuery(hql)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public BankTransaction findByUserIdAndTransactionReference(
            int userId, String transactionReference) {

        Session session = sessionFactory.getCurrentSession();

        String hql = "from BankTransaction where userId = :userId "
                + "and transactionReference = :transactionReference";

        List results = session.createQuery(hql)
                .setParameter("userId", userId)
                .setParameter("transactionReference", transactionReference)
                .setMaxResults(1)
                .list();

        if (results == null || results.isEmpty()) {
            return null;
        }

        return (BankTransaction) results.get(0);
    }

    @Override
    public void delete(int id, int userId) {
        Session session = sessionFactory.getCurrentSession();

        BankTransaction transaction =
                session.get(BankTransaction.class, id);

        if (transaction != null && transaction.getUserId() == userId) {
            session.delete(transaction);
        }
    }
}

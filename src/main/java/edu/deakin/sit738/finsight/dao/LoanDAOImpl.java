package edu.deakin.sit738.finsight.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.deakin.sit738.finsight.entity.Loan;

@Repository
public class LoanDAOImpl implements LoanDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Loan loan) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(loan);
    }

    @Override
    public List<Loan> findByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "from Loan where userId = :userId";

        return session.createQuery(hql)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();

        Loan loan = session.get(Loan.class, id);

        if (loan != null) {
            session.delete(loan);
        }
    }
}
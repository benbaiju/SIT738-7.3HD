package edu.deakin.sit738.finsight.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.deakin.sit738.finsight.entity.Investment;

@Repository
public class InvestmentDAOImpl implements InvestmentDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Investment investment) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(investment);
    }

    @Override
    public List<Investment> findByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "from Investment where userId = :userId";

        return session.createQuery(hql)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public void delete(int id, int userId) {
        Session session = sessionFactory.getCurrentSession();

        Investment investment = session.get(Investment.class, id);

        if (investment != null && investment.getUserId() == userId) {
            session.delete(investment);
        }
    }
}

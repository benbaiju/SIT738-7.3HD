package edu.deakin.sit738.finsight.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.deakin.sit738.finsight.entity.FinancialGoal;

@Repository
public class FinancialGoalDAOImpl implements FinancialGoalDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(FinancialGoal goal) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(goal);
    }

    @Override
    public List<FinancialGoal> findByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "from FinancialGoal where userId = :userId";

        return session.createQuery(hql)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public FinancialGoal findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(FinancialGoal.class, id);
    }

    @Override
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();

        FinancialGoal goal = session.get(FinancialGoal.class, id);

        if (goal != null) {
            session.delete(goal);
        }
    }
}
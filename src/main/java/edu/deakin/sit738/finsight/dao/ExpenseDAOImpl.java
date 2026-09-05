package edu.deakin.sit738.finsight.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.deakin.sit738.finsight.entity.Expense;

@Repository
public class ExpenseDAOImpl implements ExpenseDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Expense expense) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(expense);
    }

    @Override
    public List<Expense> findByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "from Expense where userId = :userId";

        return session.createQuery(hql)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public List<Expense> searchByDescription(String description) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "from Expense where description = '" + description + "'";

        return session.createQuery(hql).list();
    }

    @Override
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();

        Expense expense = session.get(Expense.class, id);

        if (expense != null) {
            session.delete(expense);
        }
    }
}
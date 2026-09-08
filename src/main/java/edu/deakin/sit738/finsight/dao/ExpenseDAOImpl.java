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
    @SuppressWarnings("unchecked")
    public List<Expense> searchByDescription(int userId, String description) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "from Expense where userId = :userId "
                + "and description = :description";

        return session.createQuery(hql)
                .setParameter("userId", userId)
                .setParameter("description", description)
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Expense> searchByDescriptionForUser(
            int userId,
            String description) {

        Session session = sessionFactory.getCurrentSession();

        return session
                .createNativeQuery(
                        "CALL search_expenses_by_description_for_user("
                                + ":userId, :description)")
                .addEntity(Expense.class)
                .setParameter("userId", userId)
                .setParameter("description", description)
                .list();
    }

    @Override
    public Expense findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Expense.class, id);
    }

    @Override
    public void delete(int id, int userId) {
        Session session = sessionFactory.getCurrentSession();

        Expense expense = session.get(Expense.class, id);

        if (expense != null && expense.getUserId() == userId) {
            session.delete(expense);
        }
    }
}

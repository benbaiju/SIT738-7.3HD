package edu.deakin.sit738.finsight.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.deakin.sit738.finsight.entity.User;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                "from User where email = :email",
                User.class
        ).setParameter("email", email)
         .uniqueResult();
    }

    @Override
    public void save(User user) {
        sessionFactory.getCurrentSession().saveOrUpdate(user);
    }

    @Override
    public User findById(int id) {
        return sessionFactory.getCurrentSession().get(User.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from User order by id")
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findByRole(String role) {
        return sessionFactory.getCurrentSession()
                .createQuery("from User where role = :role order by fullName")
                .setParameter("role", role)
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findByAdvisorId(int advisorId) {
        return sessionFactory.getCurrentSession()
                .createQuery(
                        "from User where advisorId = :advisorId order by fullName")
                .setParameter("advisorId", advisorId)
                .list();
    }
}

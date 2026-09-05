package edu.deakin.sit738.finsight.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.deakin.sit738.finsight.entity.UploadedFile;

@Repository
public class UploadedFileDAOImpl implements UploadedFileDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(UploadedFile file) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(file);
    }

    @Override
    public List<UploadedFile> findByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "from UploadedFile where userId = :userId";

        return session.createQuery(hql)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();

        UploadedFile file = session.get(UploadedFile.class, id);

        if (file != null) {
            session.delete(file);
        }
    }
}
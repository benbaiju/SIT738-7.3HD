package edu.deakin.sit738.finsight.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.deakin.sit738.finsight.dao.UserDAO;
import edu.deakin.sit738.finsight.entity.User;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Transactional
    public void save(User user) {
        userDAO.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(int id) {
        return userDAO.findById(id);
    }
}
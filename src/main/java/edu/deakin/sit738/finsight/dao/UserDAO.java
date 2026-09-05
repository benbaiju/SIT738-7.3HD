package edu.deakin.sit738.finsight.dao;

import edu.deakin.sit738.finsight.entity.User;

public interface UserDAO {

    User findByEmail(String email);

    void save(User user);

    User findById(int id);
}
package edu.deakin.sit738.finsight.dao;

import java.util.List;

import edu.deakin.sit738.finsight.entity.User;

public interface UserDAO {

    User findByEmail(String email);

    void save(User user);

    User findById(int id);

    List<User> findAll();

    List<User> findByRole(String role);

    List<User> findByAdvisorId(int advisorId);
}

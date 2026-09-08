package edu.deakin.sit738.finsight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.deakin.sit738.finsight.dao.UserDAO;
import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.security.UserRoles;

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

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userDAO.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> findAdvisors() {
        return userDAO.findByRole(UserRoles.ADVISOR);
    }

    @Transactional(readOnly = true)
    public List<User> findClientsByAdvisorId(int advisorId) {
        return userDAO.findByAdvisorId(advisorId);
    }

    @Transactional(readOnly = true)
    public boolean isAssignedClient(int advisorId, int clientId) {
        User client = userDAO.findById(clientId);

        return client != null
                && UserRoles.isUser(client.getRole())
                && client.getAdvisorId() != null
                && client.getAdvisorId().intValue() == advisorId;
    }

    @Transactional
    public void assignAdvisorToClient(int clientId, Integer advisorId) {
        User client = userDAO.findById(clientId);

        if (client == null || !UserRoles.isUser(client.getRole())) {
            throw new IllegalArgumentException("Client user was not found.");
        }

        if (advisorId == null) {
            client.setAdvisorId(null);
            userDAO.save(client);
            return;
        }

        User advisor = userDAO.findById(advisorId);

        if (advisor == null || !UserRoles.isAdvisor(advisor.getRole())) {
            throw new IllegalArgumentException(
                    "Selected advisor is invalid.");
        }

        client.setAdvisorId(advisorId);
        userDAO.save(client);
    }

    @Transactional
    public void updateUserRole(int userId, String role) {
        User user = userDAO.findById(userId);

        if (user == null) {
            throw new IllegalArgumentException("User was not found.");
        }

        String normalised = UserRoles.normalize(role);
        user.setRole(normalised);

        if (!UserRoles.isUser(normalised)) {
            user.setAdvisorId(null);
        }

        if (!UserRoles.isAdvisor(normalised)) {
            clearAdvisorAssignments(userId);
        }

        userDAO.save(user);
    }

    private void clearAdvisorAssignments(int advisorId) {
        List<User> clients = userDAO.findByAdvisorId(advisorId);

        for (User client : clients) {
            client.setAdvisorId(null);
            userDAO.save(client);
        }
    }
}

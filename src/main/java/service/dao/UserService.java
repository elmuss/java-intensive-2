package service.dao;

import model.User;

import java.util.List;

public interface UserService {
    void createUser();

    void updateUser(int id);

    User findUser(int id);

    void deleteUser(int id);

    List<User> findAllUsers();
}

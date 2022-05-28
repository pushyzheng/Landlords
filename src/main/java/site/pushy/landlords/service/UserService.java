package site.pushy.landlords.service;

import site.pushy.landlords.pojo.DO.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(String id);

    boolean updateUser(User user);
}

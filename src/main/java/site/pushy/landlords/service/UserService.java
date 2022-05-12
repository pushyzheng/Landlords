package site.pushy.landlords.service;

import site.pushy.landlords.pojo.DO.User;

public interface UserService {

    User getUserById(String id);

    boolean updateUser(User user);
}

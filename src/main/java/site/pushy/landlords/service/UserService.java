package site.pushy.landlords.service;

import lombok.Builder;
import lombok.Getter;
import site.pushy.landlords.pojo.DO.User;

public interface UserService {

    User getUserById(String id);

    User getByCondition(Condition condition);

    boolean save(User user);

    boolean updateUser(User user);

    @Getter
    @Builder
    class Condition {

        String name;

        String openId;
    }
}

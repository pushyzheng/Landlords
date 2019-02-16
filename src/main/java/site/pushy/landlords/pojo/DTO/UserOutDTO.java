package site.pushy.landlords.pojo.DTO;

import lombok.Data;
import site.pushy.landlords.pojo.DO.User;

/**
 * @author Pushy
 * @since 2019/2/16 13:53
 */
@Data
public class UserOutDTO {

    private String id;

    private String username;

    private String gender;

    private Double money;

    private String avatar;

    public UserOutDTO(User user) {
        id = user.getId();
        username = user.getUsername();
        gender = user.getGender();
        money = user.getMoney();
        avatar = user.getAvatar();
    }
}

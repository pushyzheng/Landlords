package site.pushy.landlords.pojo.DTO;

import lombok.Data;
import site.pushy.landlords.pojo.DO.User;

/**
 * 用户的公开信息
 *
 * @author Pushy
 * @since 2019/2/16 13:53
 */
@Data
public class UserOutDTO {

    private String id;

    private String username;

    private String gender;

    private String avatar;

    public static UserOutDTO fromUser(User user) {
        if (user == null) {
            return null;
        }
        UserOutDTO userOutDTO = new UserOutDTO();
        userOutDTO.setId(user.getId());
        userOutDTO.setUsername(user.getUsername());
        userOutDTO.setAvatar(user.getAvatar());
        userOutDTO.setGender(user.getGender());
        return userOutDTO;
    }
}

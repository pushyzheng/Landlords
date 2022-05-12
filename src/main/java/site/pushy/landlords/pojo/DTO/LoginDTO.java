package site.pushy.landlords.pojo.DTO;

import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * @author Pushy
 * @since 2019/1/20 11:36
 */
@Data
public class LoginDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;
}

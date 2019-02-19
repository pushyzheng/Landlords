package site.pushy.landlords.pojo.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RoomDTO {

    @NotNull
    private String id;        // 房间号

    private String password;  // 房间密码

    private String title;     // 房间标题

}

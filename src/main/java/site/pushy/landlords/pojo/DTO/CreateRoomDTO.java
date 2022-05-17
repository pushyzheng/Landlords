package site.pushy.landlords.pojo.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateRoomDTO {

    @NotBlank
    private String title;

    private String password;
}

package site.pushy.landlords.pojo.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Pushy
 * @since 2019/1/9 21:17
 */
@Data
public class ReadyGameDTO {

    @NotNull
    private String roomId;

}

package site.pushy.landlords.pojo.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Pushy
 * @since 2019/2/3 17:47
 */
@Data
public class BidDTO {

    @NotNull
    private boolean want;

}

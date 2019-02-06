package site.pushy.landlords.pojo.DTO;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Pushy
 * @since 2019/2/3 17:47
 */
@Data
public class BidDTO {

    private boolean want;

    private int score;

}

package site.pushy.landlords.pojo;

import lombok.Data;

/**
 * @author Pushy
 * @since 2019/1/24 18:09
 */
@Data
public class Round {

    private int multiple;

    private int stepNum;

    public Round() {
        this.multiple = 1;
        this.stepNum = 0;
    }
}

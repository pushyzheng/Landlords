package site.pushy.landlords.pojo;

import lombok.Data;

/**
 * 游戏回合
 *
 * @author Pushy
 * @since 2019/1/24 18:09
 */
@Data
public class Round {

    /**
     * 当前回合的倍数
     */
    private int multiple;

    /**
     * 步数
     */
    private int stepNum;

    public Round() {
        this.multiple = 1;
        this.stepNum = 0;
    }
}

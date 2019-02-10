package site.pushy.landlords.pojo.DO;

import lombok.Data;

@Data
public class Achievement {

    private Integer id;

    private Integer winMatch;

    private Integer failureMatch;

    private Integer sum;

    private String userId;

    public void incrWinMatch() {
        winMatch++;
        sum++;
    }

    public void decrFailureMatch() {
        failureMatch++;
        sum++;
    }

}
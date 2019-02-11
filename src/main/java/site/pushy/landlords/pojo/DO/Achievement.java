package site.pushy.landlords.pojo.DO;

import lombok.Data;

import java.util.UUID;

@Data
public class Achievement {

    private String id;

    private Integer winMatch;

    private Integer failureMatch;

    private Integer sum;

    private String userId;

    public Achievement() {
    }

    public Achievement(String userId) {
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.winMatch = 0;
        this.failureMatch = 0;
        this.sum = 0;
        this.userId = userId;
    }

    public void incrWinMatch() {
        winMatch++;
        sum++;
    }

    public void incrFailureMatch() {
        failureMatch++;
        sum++;
    }

}
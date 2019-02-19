package site.pushy.landlords.pojo.DO;

import lombok.Data;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Pushy
 * @since 2018/12/29 20:45
 */
@Data
public class User {

    private String id;

    private String username;

    private String password;

    private String openid;

    private String gender;

    private Double money;

    private String avatar;

    private Date timestamp;

    public User() {
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.timestamp = new Date();
        this.money = 0.0;
    }

    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String openid, String avatar) {
        this(username, password);
        this.openid = openid;
        this.avatar = avatar;
    }

    public void incrMoney(int value) {
        money += value;
    }

    public void descMoney(int value) {
        money -= value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

}
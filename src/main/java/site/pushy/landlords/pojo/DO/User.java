package site.pushy.landlords.pojo.DO;

import lombok.Data;

import java.util.Date;

/**
 * @author Pushy
 * @since 2018/12/29 20:45
 */
@Data
public class User {

    private String id;

    private String username;

    private String password;

    private String gender;

    private Double money;

    private String avatar;

    private Date timestamp;

}
package site.pushy.landlords.service;

import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Room;

/**
 * @author Pushy
 * @since 2018/12/29 21:08
 */
public interface RoomService {

    Room createRoom(User user);

    boolean joinRoom(String id, User user);

    boolean exitRoom(String id, User user);

}

package site.pushy.landlords.service;

import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.pojo.User;

/**
 * @author Pushy
 * @since 2018/12/29 21:08
 */
public interface RoomService {

    Room createRoom(User user);

    boolean joinRoom(String id, User user);

    boolean exitRoom(String id, User user);

}

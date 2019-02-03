package site.pushy.landlords.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.service.PlayerService;

import java.util.List;

/**
 * @author Pushy
 * @since 2019/2/1 18:21
 */
@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private RoomComponent roomComponent;

    @Override
    public List<Card> getPlayerCards(User curUser) {
        return roomComponent.getUserCards(curUser.getId());
    }
}

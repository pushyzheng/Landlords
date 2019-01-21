package site.pushy.landlords.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.ReadyGameDTO;
import site.pushy.landlords.service.GameService;

import javax.validation.Valid;

/**
 * @author Pushy
 * @since 2019/1/9 21:16
 */
@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/ready")
    public String readyGame(@SessionAttribute User curUser,
                            @Valid @RequestBody ReadyGameDTO readyGameDTO) {
        boolean start = gameService.readyGame(curUser, readyGameDTO);
        if (start) {
            gameService.startGame(readyGameDTO.getRoomId());
        }
        return RespEntity.success(start);
    }

}

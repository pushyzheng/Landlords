package site.pushy.landlords.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pushy
 * @since 2018/12/29 20:34
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {

    @GetMapping()
    public String listRoom() {
        return "";
    }
}

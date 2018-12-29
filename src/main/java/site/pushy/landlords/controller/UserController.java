package site.pushy.landlords.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pushy
 * @since 2018/12/29 19:24
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("")
    public String test() {
        return "hello user";
    }
}

package site.pushy.landlords.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.core.component.NotifyComponent;
import site.pushy.landlords.pojo.DO.User;

/**
 * @author Pushy
 * @since 2019/2/10 21:14
 */
@RestController
@RequestMapping(value = "/test", produces = "application/json")
public class TestController {

    @Autowired
    private NotifyComponent notifyComponent;

    private int num = 0;

    @RequestMapping("/sendToUserMessage")
    public String sendToUserMessage(@RequestParam String userId, String message) {
        boolean res = notifyComponent.sendToUser(userId, message == null ? String.valueOf(num++) : message);
        return RespEntity.success(res);
    }

    @GetMapping("/test1")
    public String test() {
        return RespEntity.success(new User());
    }
}
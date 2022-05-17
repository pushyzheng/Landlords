package site.pushy.landlords.controller;

import org.springframework.web.bind.annotation.*;
import site.pushy.landlords.common.exception.UnauthorizedException;
import site.pushy.landlords.pojo.ApiResponse;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.UserDTO;
import site.pushy.landlords.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Fuxing
 * @since 2019年1月19日11:29:18
 */
@RestController
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/myself")
    public ApiResponse<User> getMyUser(@SessionAttribute User curUser) {
        return ApiResponse.success(curUser);
    }

    @PutMapping("")
    public ApiResponse<Boolean> updateUser(@SessionAttribute User curUser, HttpServletRequest request,
                                           @Valid @RequestBody UserDTO userDTO) {
        User record = userService.getUserById(curUser.getId());
        if (record == null) {
            throw new UnauthorizedException("用户信息为空");
        }
        record.setUsername(userDTO.getUsername());
        record.setPassword(userDTO.getPassword());
        record.setAvatar(userDTO.getAvatar());
        record.setGender(userDTO.getGender());
        // 更新 session 中的用户信息
        request.getSession().setAttribute("curUser", record);
        return ApiResponse.success(userService.updateUser(record));
    }
}

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
import java.util.Optional;

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
        Optional<User> record = userService.getUserById(curUser.getId());
        if (!record.isPresent()) {
            throw new UnauthorizedException("用户信息为空");
        }
        record.get().setUsername(userDTO.getUsername());
        record.get().setPassword(userDTO.getPassword());
        record.get().setAvatar(userDTO.getAvatar());
        record.get().setGender(userDTO.getGender());
        // 更新 session 中的用户信息
        request.getSession().setAttribute("curUser", record.get());
        return ApiResponse.success(userService.updateUser(record.get()));
    }
}

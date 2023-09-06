package com.zhaofn.twitch.user;

import com.zhaofn.twitch.model.RegisterBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)//default就是这个所以这句没必要
    public void register(@RequestBody RegisterBody body) {//body是加密的，而request parameters会显示在url上
        userService.register(body.username(), body.password(), body.firstName(), body.lastName());
    }

}

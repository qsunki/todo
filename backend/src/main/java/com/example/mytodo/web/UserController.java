package com.example.mytodo.web;

import com.example.mytodo.user.application.UserDetail;
import com.example.mytodo.user.application.UserJoinService;
import com.example.mytodo.user.application.command.UserJoinReq;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserController {
    private final UserJoinService userJoinService;

    UserController(UserJoinService userJoinService) {
        this.userJoinService = userJoinService;
    }

    @PostMapping("/api/users")
    UserDetail join(@RequestBody UserJoinReq userJoinReq) {
        return userJoinService.join(userJoinReq);
    }
}

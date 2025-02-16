package com.example.mytodo.web;

import com.example.mytodo.infra.security.UserSessionInfo;
import com.example.mytodo.user.application.UserDetail;
import com.example.mytodo.user.application.UserJoinService;
import com.example.mytodo.user.application.command.UserJoinReq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserController {
    private final UserJoinService userJoinService;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    UserController(UserJoinService userJoinService) {
        this.userJoinService = userJoinService;
    }

    @PostMapping("/api/users")
    UserDetail join(@RequestBody UserJoinReq userJoinReq) {
        return userJoinService.join(userJoinReq);
    }

    @GetMapping("/api/users/me")
    UserDetail join(@AuthenticationPrincipal UserSessionInfo userSessionInfo) {
        return new UserDetail(userSessionInfo.username());
    }

    @GetMapping(value = "/api/logout")
    public void logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        if (authentication != null) {
            logoutHandler.logout(request, response, authentication);
        }
    }
}

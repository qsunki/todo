package com.example.mytodo.user.application;

import com.example.mytodo.user.application.command.UserJoinReq;
import com.example.mytodo.user.application.exception.UserDuplicateException;
import com.example.mytodo.user.domain.User;
import com.example.mytodo.user.domain.UserRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserJoinService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserJoinService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetail join(@NonNull UserJoinReq userJoinReq) {
        if (userRepository.existsByUsername(userJoinReq.username())) {
            throw new UserDuplicateException(userJoinReq.username() + " already exists");
        }
        User user = new User(null, userJoinReq.username(), passwordEncoder.encode(userJoinReq.password()));
        User save = userRepository.save(user);
        return new UserDetail(save.getUsername());
    }
}

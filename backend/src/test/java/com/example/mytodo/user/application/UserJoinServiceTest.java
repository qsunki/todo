package com.example.mytodo.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.mytodo.TestcontainersConfiguration;
import com.example.mytodo.user.application.command.UserJoinReq;
import com.example.mytodo.user.application.exception.UserDuplicateException;
import com.example.mytodo.user.domain.User;
import com.example.mytodo.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DataJdbcTest
@Import(TestcontainersConfiguration.class)
class UserJoinServiceTest {

    @Autowired
    UserRepository userRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    UserJoinService userJoinService;

    @BeforeEach
    void setUp() {
        userJoinService = new UserJoinService(userRepository, passwordEncoder);
    }

    @DisplayName("중복된 username으로 회원가입할 수 없다")
    @Test
    void if_username_is_duplicated_then_throw() {
        // given
        User existingUser = new User(null, "newUser", passwordEncoder.encode("password"));
        userRepository.save(existingUser);
        UserJoinReq userJoinReq = new UserJoinReq(existingUser.getUsername(), "password1234!");

        // when & then
        assertThatThrownBy(() -> userJoinService.join(userJoinReq)).isInstanceOf(UserDuplicateException.class);
    }

    @Test
    void join() {
        // given
        UserJoinReq userJoinReq = new UserJoinReq("newUser", "password1234!");

        // when
        UserDetail userDetail = userJoinService.join(userJoinReq);

        // then
        assertThat(userDetail).isNotNull().extracting(UserDetail::username).isEqualTo(userJoinReq.username());
        assertThat(userRepository.findByUsername(userJoinReq.username()))
                .isPresent()
                .get()
                .satisfies(user -> {
                    assertThat(user.getUsername()).isEqualTo(userJoinReq.username());
                    assertThat(passwordEncoder.matches(userJoinReq.password(), user.getPassword()))
                            .isTrue();
                });
    }
}

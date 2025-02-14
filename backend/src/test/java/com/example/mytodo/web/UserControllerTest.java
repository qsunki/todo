package com.example.mytodo.web;

import com.example.mytodo.TestcontainersConfiguration;
import com.example.mytodo.user.application.UserDetail;
import com.example.mytodo.user.application.command.UserJoinReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    int port;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient =
                WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void join() {
        // given
        UserJoinReq request = new UserJoinReq("testUser", "1234");

        // when & then
        webTestClient
                .post()
                .uri("/api/users")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserDetail.class)
                .isEqualTo(new UserDetail(request.username()));
    }
}

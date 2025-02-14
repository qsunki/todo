package com.example.mytodo.web;

import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

import com.example.mytodo.TestcontainersConfiguration;
import com.example.mytodo.infra.security.UserLoginReq;
import com.example.mytodo.user.application.UserDetail;
import com.example.mytodo.user.application.command.UserJoinReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@Import(TestcontainersConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    int port;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        webTestClient = ControllerTestHelper.createWebTestClient(port, restDocumentation);
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
                .isEqualTo(new UserDetail(request.username()))
                .consumeWith(document("user/{method-name}"));
    }

    @Test
    void login() {
        // given
        UserLoginReq request = new UserLoginReq("user1", "1234");

        // when & then
        webTestClient
                .post()
                .uri("/api/login")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserDetail.class)
                .isEqualTo(new UserDetail(request.username()))
                .consumeWith(document("user/{method-name}"));
    }
}

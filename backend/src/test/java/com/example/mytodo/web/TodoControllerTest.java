package com.example.mytodo.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.mytodo.TestcontainersConfiguration;
import com.example.mytodo.infra.security.UserLoginReq;
import com.example.mytodo.todo.application.TodoDetail;
import com.example.mytodo.todo.application.command.TodoCreateReq;
import com.example.mytodo.todo.application.command.TodoUpdateReq;
import com.example.mytodo.user.application.UserDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoControllerTest {

    @LocalServerPort
    int port;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient =
                WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void create() {
        // given
        String sessionId = getLoginSession();
        TodoCreateReq request = new TodoCreateReq("something to do");

        // when & then
        webTestClient
                .mutate()
                .defaultCookie("JSESSIONID", sessionId)
                .build()
                .post()
                .uri("/api/todos")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TodoDetail.class)
                .value(todoDetail -> {
                    assertThat(todoDetail.id()).isNotNull();
                    assertThat(todoDetail.content()).isEqualTo(request.content());
                    assertThat(todoDetail.complete()).isFalse();
                });
    }

    @Test
    void update() {
        // given
        String sessionId = getLoginSession();
        TodoDetail createdTodo = createTodo(sessionId);
        TodoUpdateReq request = new TodoUpdateReq("content", "new Content", null);

        // when & then
        webTestClient
                .mutate()
                .defaultCookie("JSESSIONID", sessionId)
                .build()
                .patch()
                .uri("/api/todos/{todoId}", createdTodo.id())
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TodoDetail.class)
                .value(todoDetail -> {
                    assertThat(todoDetail.id()).isNotNull();
                    assertThat(todoDetail.content()).isEqualTo(request.content());
                    assertThat(todoDetail.complete()).isFalse();
                });
    }

    @Test
    void delete() {
        // given
        String sessionId = getLoginSession();
        TodoDetail createdTodo = createTodo(sessionId);

        // when & then
        webTestClient
                .mutate()
                .defaultCookie("JSESSIONID", sessionId)
                .build()
                .delete()
                .uri("/api/todos/{todoId}", createdTodo.id())
                .exchange()
                .expectStatus()
                .isOk();
    }

    private TodoDetail createTodo(String sessionId) {
        TodoCreateReq createReq = new TodoCreateReq("something to do");
        return webTestClient
                .mutate()
                .defaultCookie("JSESSIONID", sessionId)
                .build()
                .post()
                .uri("/api/todos")
                .bodyValue(createReq)
                .exchange()
                .expectBody(TodoDetail.class)
                .returnResult()
                .getResponseBody();
    }

    private String getLoginSession() {
        return webTestClient
                .post()
                .uri("/api/login")
                .bodyValue(new UserLoginReq("user1", "1234"))
                .exchange()
                .returnResult(UserDetail.class)
                .getResponseCookies()
                .get("JSESSIONID")
                .getFirst()
                .getValue();
    }
}

package com.example.mytodo.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

import com.example.mytodo.TestcontainersConfiguration;
import com.example.mytodo.property.domain.PropertyType;
import com.example.mytodo.property.domain.SelectProperty;
import com.example.mytodo.todo.application.TodoDetail;
import com.example.mytodo.todo.application.command.TodoCreateReq;
import com.example.mytodo.todo.application.command.TodoUpdateReq;
import com.example.mytodo.web.TodoListRetrieveReq.Filter;
import java.util.List;
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
class TodoControllerTest {

    @LocalServerPort
    int port;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        webTestClient = ControllerTestHelper.createWebTestClient(port, restDocumentation);
    }

    @Test
    void query() {
        // given
        String sessionId = ControllerTestHelper.getLoginSession(webTestClient);
        List<Filter> filters = List.of(new Filter("우선순위", PropertyType.SELECT, new SelectProperty("상", null)));
        TodoListRetrieveReq todoListRetrieveReq = new TodoListRetrieveReq(filters);

        // when & then
        webTestClient
                .mutate()
                .defaultCookie("JSESSIONID", sessionId)
                .build()
                .post()
                .uri("/api/todos/query")
                .bodyValue(todoListRetrieveReq)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(document("todo/{method-name}"));
    }

    @Test
    void create() {
        // given
        String sessionId = ControllerTestHelper.getLoginSession(webTestClient);
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
                })
                .consumeWith(document("todo/{method-name}"));
    }

    @Test
    void update() {
        // given
        String sessionId = ControllerTestHelper.getLoginSession(webTestClient);
        TodoDetail createdTodo = ControllerTestHelper.createTodo(webTestClient, sessionId);
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
                })
                .consumeWith(document("todo/{method-name}"));
    }

    @Test
    void delete() {
        // given
        String sessionId = ControllerTestHelper.getLoginSession(webTestClient);
        TodoDetail createdTodo = ControllerTestHelper.createTodo(webTestClient, sessionId);

        // when & then
        webTestClient
                .mutate()
                .defaultCookie("JSESSIONID", sessionId)
                .build()
                .delete()
                .uri("/api/todos/{todoId}", createdTodo.id())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Void.class)
                .consumeWith(document("todo/{method-name}"));
    }
}

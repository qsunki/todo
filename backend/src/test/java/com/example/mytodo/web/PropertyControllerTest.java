package com.example.mytodo.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

import com.example.mytodo.TestcontainersConfiguration;
import com.example.mytodo.property.application.PropertyDetail;
import com.example.mytodo.property.application.command.PropertyCreateReq;
import com.example.mytodo.property.domain.DateProperty;
import com.example.mytodo.property.domain.PropertyType;
import com.example.mytodo.property.domain.SelectProperty;
import com.example.mytodo.todo.application.TodoDetail;
import java.time.LocalDateTime;
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
class PropertyControllerTest {

    @LocalServerPort
    int port;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        webTestClient = ControllerTestHelper.createWebTestClient(port, restDocumentation);
    }

    @Test
    void date_create() {
        // given
        String sessionId = ControllerTestHelper.getLoginSession(webTestClient);
        TodoDetail createdTodo = ControllerTestHelper.createTodo(webTestClient, sessionId);
        PropertyCreateReq request = new PropertyCreateReq(
                createdTodo.id(),
                "마감일",
                PropertyType.DATE,
                new DateProperty(LocalDateTime.of(2025, 2, 17, 0, 0), null));

        // when & then
        webTestClient
                .mutate()
                .defaultCookie("JSESSIONID", sessionId)
                .build()
                .post()
                .uri("/api/todos/{todoId}/properties", createdTodo.id())
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PropertyDetail.class)
                .value(propertyDetail -> {
                    assertThat(propertyDetail.id()).isNotNull();
                    assertThat(propertyDetail.name()).isEqualTo(request.name());
                    assertThat(propertyDetail.type()).isEqualTo(request.type());
                    assertThat(propertyDetail.data()).isEqualTo(request.data());
                    assertThat(propertyDetail.todoId()).isEqualTo(createdTodo.id());
                })
                .consumeWith(document("property/{method-name}"));
    }

    @Test
    void select_create() {
        // given
        String sessionId = ControllerTestHelper.getLoginSession(webTestClient);
        TodoDetail createdTodo = ControllerTestHelper.createTodo(webTestClient, sessionId);
        PropertyCreateReq request =
                new PropertyCreateReq(createdTodo.id(), "분류", PropertyType.SELECT, new SelectProperty("백엔드", "purple"));

        // when & then
        webTestClient
                .mutate()
                .defaultCookie("JSESSIONID", sessionId)
                .build()
                .post()
                .uri("/api/todos/{todoId}/properties", createdTodo.id())
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PropertyDetail.class)
                .value(propertyDetail -> {
                    assertThat(propertyDetail.id()).isNotNull();
                    assertThat(propertyDetail.name()).isEqualTo(request.name());
                    assertThat(propertyDetail.type()).isEqualTo(request.type());
                    assertThat(propertyDetail.data()).isEqualTo(request.data());
                    assertThat(propertyDetail.todoId()).isEqualTo(createdTodo.id());
                })
                .consumeWith(document("property/{method-name}"));
    }
}

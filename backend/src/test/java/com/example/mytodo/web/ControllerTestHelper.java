package com.example.mytodo.web;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import com.example.mytodo.infra.security.UserLoginReq;
import com.example.mytodo.todo.application.TodoDetail;
import com.example.mytodo.todo.application.command.TodoCreateReq;
import com.example.mytodo.user.application.UserDetail;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.reactive.server.WebTestClient;

public class ControllerTestHelper {
    public static WebTestClient createWebTestClient(int port, RestDocumentationContextProvider restDocumentation) {
        return WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .filter(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(
                                prettyPrint(),
                                modifyUris().port(8080),
                                modifyHeaders()
                                        .remove("accept-encoding")
                                        .remove("user-agent")
                                        .remove("accept")
                                        .remove("Content-Length")
                                        .remove("Host"))
                        .withResponseDefaults(
                                prettyPrint(),
                                modifyHeaders()
                                        .remove("X-Content-Type-Options")
                                        .remove("X-XSS-Protection")
                                        .remove("Cache-Control")
                                        .remove("Pragma")
                                        .remove("Expires")
                                        .remove("X-Frame-Options")
                                        .remove("Transfer-Encoding")
                                        .remove("Date")
                                        .remove("Content-Length")))
                .build();
    }

    public static String getLoginSession(WebTestClient webTestClient) {
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

    public static TodoDetail createTodo(WebTestClient webTestClient, String sessionId) {
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
}

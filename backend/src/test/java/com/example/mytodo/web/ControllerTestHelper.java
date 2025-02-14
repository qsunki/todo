package com.example.mytodo.web;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

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
}

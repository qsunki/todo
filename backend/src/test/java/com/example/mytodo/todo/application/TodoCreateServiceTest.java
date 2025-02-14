package com.example.mytodo.todo.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.mytodo.TestcontainersConfiguration;
import com.example.mytodo.todo.application.command.TodoCreateReq;
import com.example.mytodo.todo.domain.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

@DataJdbcTest
@Import(TestcontainersConfiguration.class)
class TodoCreateServiceTest {

    @Autowired
    TodoRepository todoRepository;

    TodoCreateService todoCreateService;

    @BeforeEach
    void setUp() {
        todoCreateService = new TodoCreateService(todoRepository);
    }

    @DisplayName("content가 null이면 빈 문자열로 생성한다")
    @Test
    void create_when_content_is_null() {
        // given
        Long userId = 1L;
        TodoCreateReq todoCreateReq = new TodoCreateReq(null);

        // when
        TodoDetail todoDetail = todoCreateService.create(todoCreateReq, userId);

        // then
        assertThat(todoDetail).isNotNull().extracting(TodoDetail::content).isEqualTo("");
    }

    @Test
    void create() {
        // given
        Long userId = 1L;
        TodoCreateReq todoCreateReq = new TodoCreateReq("first todo");

        // when
        TodoDetail todoDetail = todoCreateService.create(todoCreateReq, userId);

        // then
        assertThat(todoDetail).isNotNull().extracting(TodoDetail::content).isEqualTo(todoCreateReq.content());
    }
}

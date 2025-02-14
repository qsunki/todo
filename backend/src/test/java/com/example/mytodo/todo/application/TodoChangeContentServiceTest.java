package com.example.mytodo.todo.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.mytodo.TestcontainersConfiguration;
import com.example.mytodo.todo.application.command.TodoChangeContentReq;
import com.example.mytodo.todo.application.exception.AccessDeniedException;
import com.example.mytodo.todo.domain.Todo;
import com.example.mytodo.todo.domain.TodoRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

@DataJdbcTest
@Import(TestcontainersConfiguration.class)
class TodoChangeContentServiceTest {

    @Autowired
    TodoRepository todoRepository;

    TodoChangeContentService todoChangeContentService;

    @BeforeEach
    void setUp() {
        todoChangeContentService = new TodoChangeContentService(todoRepository);
    }

    @DisplayName("자신의 todo가 아닌 것을 수정할 수 없다")
    @Test
    void if_not_users_own_then_throw() {
        // given
        Long owner = 1L;
        Todo oldTodo = createTodo(owner);
        Long someoneElse = 2L;
        TodoChangeContentReq todoChangeContentReq = new TodoChangeContentReq(oldTodo.getId(), "new content");

        // when & then
        assertThatThrownBy(() -> todoChangeContentService.changeContent(todoChangeContentReq, someoneElse))
                .isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("todoId가 null이 아니여야 한다")
    @Test
    void if_id_is_null_then_throw() {
        // given
        Long userId = 1L;
        TodoChangeContentReq todoChangeContentReq = new TodoChangeContentReq(null, "new content");

        // when & then
        assertThatThrownBy(() -> todoChangeContentService.changeContent(todoChangeContentReq, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("content가 null이면 빈 문자열로 업데이트 한다")
    @Test
    void changeContent_to_empty_when_content_null() {
        // given
        Long userId = 1L;
        Todo oldTodo = createTodo(userId);
        TodoChangeContentReq todoChangeContentReq = new TodoChangeContentReq(oldTodo.getId(), null);

        // when
        TodoDetail todoDetail = todoChangeContentService.changeContent(todoChangeContentReq, userId);

        // then
        assertThat(todoDetail).isNotNull().extracting(TodoDetail::content).isEqualTo("");
    }

    @Test
    void changeContent() {
        // given
        Long userId = 1L;
        Todo oldTodo = createTodo(userId);
        TodoChangeContentReq todoChangeContentReq = new TodoChangeContentReq(oldTodo.getId(), "new content");

        // when
        TodoDetail todoDetail = todoChangeContentService.changeContent(todoChangeContentReq, userId);

        // then
        assertThat(todoDetail).isNotNull().extracting(TodoDetail::content).isEqualTo(todoChangeContentReq.content());
        assertThat(todoRepository.findById(todoDetail.id())).get().satisfies(todo -> {
            assertThat(todo.getContent()).isEqualTo(todoChangeContentReq.content());
            assertThat(todo.getLastModifiedTime()).isNotEqualTo(oldTodo.getLastModifiedTime());
        });
    }

    private Todo createTodo(Long userId) {
        return todoRepository.save(new Todo(
                null,
                "old content",
                false,
                LocalDateTime.of(2025, 2, 13, 1, 0),
                LocalDateTime.of(2025, 2, 13, 1, 0),
                userId));
    }
}

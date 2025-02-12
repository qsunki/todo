package com.example.mytodo.todo.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.mytodo.TestcontainersConfiguration;
import com.example.mytodo.todo.application.dto.TodoDetail;
import com.example.mytodo.todo.application.dto.TodoUpdateReq;
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
class TodoUpdateServiceTest {

    @Autowired
    TodoRepository todoRepository;

    TodoUpdateService todoUpdateService;

    @BeforeEach
    void setUp() {
        todoUpdateService = new TodoUpdateService(todoRepository);
    }

    @DisplayName("자신의 todo가 아닌 것을 수정할 수 없다")
    @Test
    void if_not_users_own_then_throw() {
        // given
        Long owner = 1L;
        Todo oldTodo = createTodo(owner);
        Long someoneElse = 2L;
        TodoUpdateReq todoUpdateReq = new TodoUpdateReq(oldTodo.getId(), "new content");

        // when & then
        assertThatThrownBy(() -> todoUpdateService.update(todoUpdateReq, someoneElse))
                .isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("todoId가 null이 아니여야 한다")
    @Test
    void if_id_is_null_then_throw() {
        // given
        Long userId = 1L;
        TodoUpdateReq todoUpdateReq = new TodoUpdateReq(null, "new content");

        // when & then
        assertThatThrownBy(() -> todoUpdateService.update(todoUpdateReq, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("content가 null이면 빈 문자열로 업데이트 한다")
    @Test
    void update_when_content_null() {
        // given
        Long userId = 1L;
        Todo oldTodo = createTodo(userId);
        TodoUpdateReq todoUpdateReq = new TodoUpdateReq(oldTodo.getId(), null);

        // when
        TodoDetail todoDetail = todoUpdateService.update(todoUpdateReq, userId);

        // then
        assertThat(todoDetail).isNotNull().extracting(TodoDetail::content).isEqualTo("");
    }

    @Test
    void update() {
        // given
        Long userId = 1L;
        Todo oldTodo = createTodo(userId);
        TodoUpdateReq todoUpdateReq = new TodoUpdateReq(oldTodo.getId(), "new content");

        // when
        TodoDetail todoDetail = todoUpdateService.update(todoUpdateReq, userId);

        // then
        assertThat(todoDetail).isNotNull().extracting(TodoDetail::content).isEqualTo(todoUpdateReq.content());
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

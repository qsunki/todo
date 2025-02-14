package com.example.mytodo.todo.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.mytodo.TestcontainersConfiguration;
import com.example.mytodo.todo.application.dto.TodoDeleteReq;
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
class TodoDeleteServiceTest {
    @Autowired
    TodoRepository todoRepository;

    TodoDeleteService todoDeleteService;

    @BeforeEach
    void setUp() {
        todoDeleteService = new TodoDeleteService(todoRepository);
    }

    @DisplayName("자신의 todo가 아닌 것을 삭제할 수 없다")
    @Test
    void if_not_users_own_then_throw() {
        // given
        Long owner = 1L;
        Todo todo = createTodo(owner);
        Long stranger = 2L;
        TodoDeleteReq todoDeleteReq = new TodoDeleteReq(todo.getId());

        // when & then
        assertThatThrownBy(() -> todoDeleteService.delete(todoDeleteReq, stranger))
                .isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("todoId가 null이 아니여야 한다")
    @Test
    void if_id_is_null_then_throw() {
        // given
        Long userId = 1L;
        TodoDeleteReq todoDeleteReq = new TodoDeleteReq(null);

        // when & then
        assertThatThrownBy(() -> todoDeleteService.delete(todoDeleteReq, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void delete() {
        // given
        Long userId = 1L;
        Todo todo = createTodo(userId);
        TodoDeleteReq todoDeleteReq = new TodoDeleteReq(todo.getId());

        // when
        todoDeleteService.delete(todoDeleteReq, userId);

        // then
        assertThat(todoRepository.findById(todo.getId())).isEmpty();
    }

    private Todo createTodo(Long userId) {
        return todoRepository.save(new Todo(
                null,
                "something todo",
                false,
                LocalDateTime.of(2025, 2, 13, 1, 0),
                LocalDateTime.of(2025, 2, 13, 1, 0),
                userId));
    }
}

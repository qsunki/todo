package com.example.mytodo.todo.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.mytodo.TestcontainersConfiguration;
import com.example.mytodo.todo.application.command.TodoChangeCompletionReq;
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
class TodoChangeCompletionServiceTest {

    @Autowired
    TodoRepository todoRepository;

    TodoChangeCompletionService todoChangeCompletionService;

    @BeforeEach
    void setUp() {
        todoChangeCompletionService = new TodoChangeCompletionService(todoRepository);
    }

    @DisplayName("자신의 todo가 아닌 것을 완료할 수 없다")
    @Test
    void if_not_users_own_then_throw() {
        // given
        Long owner = 1L;
        Todo oldTodo = createIncompleteTodo(owner);
        Long someoneElse = 2L;
        TodoChangeCompletionReq todoChangeCompletionReq = new TodoChangeCompletionReq(oldTodo.getId(), true);

        // when & then
        assertThatThrownBy(() -> todoChangeCompletionService.changeCompletion(todoChangeCompletionReq, someoneElse))
                .isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("todoId가 null이 아니여야 한다")
    @Test
    void if_id_is_null_then_throw() {
        // given
        Long userId = 1L;
        TodoChangeCompletionReq todoChangeCompletionReq = new TodoChangeCompletionReq(null, true);

        // when & then
        assertThatThrownBy(() -> todoChangeCompletionService.changeCompletion(todoChangeCompletionReq, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("complete가 null이 아니여야 한다")
    @Test
    void if_complete_is_null_then_throw() {
        // given
        Long userId = 1L;
        Todo oldTodo = createIncompleteTodo(userId);
        TodoChangeCompletionReq todoChangeCompletionReq = new TodoChangeCompletionReq(oldTodo.getId(), null);

        // when & then
        assertThatThrownBy(() -> todoChangeCompletionService.changeCompletion(todoChangeCompletionReq, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeCompletion() {
        // given
        Long userId = 1L;
        Todo oldTodo = createIncompleteTodo(userId);
        TodoChangeCompletionReq todoChangeCompletionReq = new TodoChangeCompletionReq(oldTodo.getId(), true);

        // when
        TodoDetail todoDetail = todoChangeCompletionService.changeCompletion(todoChangeCompletionReq, userId);

        // then
        assertThat(todoDetail)
                .isNotNull()
                .extracting(TodoDetail::complete)
                .isEqualTo(todoChangeCompletionReq.complete());
        assertThat(todoRepository.findById(todoDetail.id())).get().satisfies(todo -> {
            assertThat(todo.isCompleted()).isEqualTo(todoChangeCompletionReq.complete());
            assertThat(todo.getLastModifiedTime()).isNotEqualTo(oldTodo.getLastModifiedTime());
        });
    }

    private Todo createIncompleteTodo(Long userId) {
        return todoRepository.save(new Todo(
                null,
                "something todo",
                false,
                LocalDateTime.of(2025, 2, 13, 1, 0),
                LocalDateTime.of(2025, 2, 13, 1, 0),
                userId));
    }
}

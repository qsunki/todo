package com.example.mytodo.property.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.mytodo.TestcontainersConfiguration;
import com.example.mytodo.property.application.command.PropertyCreateReq;
import com.example.mytodo.property.domain.DateProperty;
import com.example.mytodo.property.domain.PropertyRepository;
import com.example.mytodo.property.domain.PropertyType;
import com.example.mytodo.property.domain.SelectProperty;
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
class PropertyCreateServiceTest {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    PropertyRepository propertyRepository;

    PropertyCreateService propertyCreateService;

    @BeforeEach
    void setUp() {
        propertyCreateService = new PropertyCreateService(propertyRepository, todoRepository);
    }

    @DisplayName("property의 name이 빈 문자열이면 안된다")
    @Test
    void if_property_name_is_empty_then_throw() {
        // given
        Long userId = 1L;
        Todo todo = createTodo(userId);
        PropertyCreateReq propertyCreateReq =
                new PropertyCreateReq(todo.getId(), "", PropertyType.SELECT, new SelectProperty("백엔드", "purple"));

        // when & then
        assertThatThrownBy(() -> propertyCreateService.create(propertyCreateReq, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("property의 name이 공백이면 안된다")
    @Test
    void if_property_name_is_blank_then_throw() {
        // given
        Long userId = 1L;
        Todo todo = createTodo(userId);
        PropertyCreateReq propertyCreateReq =
                new PropertyCreateReq(todo.getId(), "  ", PropertyType.SELECT, new SelectProperty("백엔드", "purple"));

        // when & then
        assertThatThrownBy(() -> propertyCreateService.create(propertyCreateReq, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("selectProperty의 name이 빈 문자열이면 안된다")
    @Test
    void if_selectProperty_name_is_empty_then_throw() {
        // given
        Long userId = 1L;
        Todo todo = createTodo(userId);
        PropertyCreateReq propertyCreateReq =
                new PropertyCreateReq(todo.getId(), "분야", PropertyType.SELECT, new SelectProperty("", "purple"));

        // when & then
        assertThatThrownBy(() -> propertyCreateService.create(propertyCreateReq, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("selectProperty의 name이 공백이면 안된다")
    @Test
    void if_selectProperty_name_is_blank_then_throw() {
        // given
        Long userId = 1L;
        Todo todo = createTodo(userId);
        PropertyCreateReq propertyCreateReq =
                new PropertyCreateReq(todo.getId(), "분야", PropertyType.SELECT, new SelectProperty(" ", "purple"));

        // when & then
        assertThatThrownBy(() -> propertyCreateService.create(propertyCreateReq, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("dateProperty의 name이 빈 문자열이면 안된다")
    @Test
    void if_dateProperty_name_is_empty_then_throw() {
        // given
        Long userId = 1L;
        Todo todo = createTodo(userId);
        PropertyCreateReq propertyCreateReq = new PropertyCreateReq(
                todo.getId(), "", PropertyType.DATE, new DateProperty(LocalDateTime.of(2025, 2, 17, 0, 0), null));

        // when & then
        assertThatThrownBy(() -> propertyCreateService.create(propertyCreateReq, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("dateProperty의 name이 공백이면 안된다")
    @Test
    void if_dateProperty_name_is_blank_then_throw() {
        // given
        Long userId = 1L;
        Todo todo = createTodo(userId);
        PropertyCreateReq propertyCreateReq = new PropertyCreateReq(
                todo.getId(), "  ", PropertyType.DATE, new DateProperty(LocalDateTime.of(2025, 2, 17, 0, 0), null));

        // when & then
        assertThatThrownBy(() -> propertyCreateService.create(propertyCreateReq, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("dateProperty의 start가 null이면 안된다")
    @Test
    void if_dateProperty_start_is_null_then_throw() {
        // given
        Long userId = 1L;
        Todo todo = createTodo(userId);
        PropertyCreateReq propertyCreateReq =
                new PropertyCreateReq(todo.getId(), "마감일", PropertyType.DATE, new DateProperty(null, null));

        // when & then
        assertThatThrownBy(() -> propertyCreateService.create(propertyCreateReq, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("자신의 todo가 아닌 것에 property를 추가할 수 없다")
    @Test
    void if_not_users_own_then_throw() {
        // given
        Long userId = 1L;
        Todo todo = createTodo(userId);
        Long strangerId = 2L;
        PropertyCreateReq propertyCreateReq =
                new PropertyCreateReq(todo.getId(), "분야", PropertyType.SELECT, new SelectProperty("백엔드", "purple"));

        // when & then
        assertThatThrownBy(() -> propertyCreateService.create(propertyCreateReq, strangerId))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void create_select_property() {
        // given
        Long userId = 1L;
        Long todoId = 1L;
        PropertyCreateReq propertyCreateReq =
                new PropertyCreateReq(todoId, "분야", PropertyType.SELECT, new SelectProperty("백엔드", "purple"));

        // when
        PropertyDetail propertyDetail = propertyCreateService.create(propertyCreateReq, userId);

        // then
        assertThat(propertyDetail).isNotNull();
        assertThat(propertyRepository.findById(propertyDetail.id()))
                .map(PropertyDetail::from)
                .get()
                .isEqualTo(propertyDetail);
    }

    @Test
    void create_date_property() {
        // given
        Long userId = 1L;
        Long todoId = 1L;
        PropertyCreateReq propertyCreateReq = new PropertyCreateReq(
                todoId, "마감일", PropertyType.DATE, new DateProperty(LocalDateTime.of(2025, 2, 17, 0, 0), null));

        // when
        PropertyDetail propertyDetail = propertyCreateService.create(propertyCreateReq, userId);

        // then
        assertThat(propertyDetail).isNotNull();
        assertThat(propertyRepository.findById(propertyDetail.id()))
                .map(PropertyDetail::from)
                .get()
                .isEqualTo(propertyDetail);
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

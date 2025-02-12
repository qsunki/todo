package com.example.mytodo.todo.application;

import com.example.mytodo.todo.application.dto.TodoCreateReq;
import com.example.mytodo.todo.domain.Todo;
import com.example.mytodo.todo.domain.TodoRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class TodoCreateService {

    private final TodoRepository todoRepository;

    public TodoCreateService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoDetail create(TodoCreateReq todoCreateReq, Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        if (todoCreateReq.content() == null) {
            todoCreateReq = new TodoCreateReq("");
        }
        Todo todo = new Todo(null, todoCreateReq.content(), false, LocalDateTime.now(), LocalDateTime.now(), userId);
        todoRepository.save(todo);
        return TodoDetail.from(todo);
    }
}

package com.example.mytodo.todo.application;

import com.example.mytodo.todo.application.command.TodoCreateReq;
import com.example.mytodo.todo.domain.Todo;
import com.example.mytodo.todo.domain.TodoRepository;
import java.time.LocalDateTime;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class TodoCreateService {

    private final TodoRepository todoRepository;

    public TodoCreateService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoDetail create(@NonNull TodoCreateReq todoCreateReq, @NonNull Long userId) {
        if (todoCreateReq.content() == null) {
            todoCreateReq = new TodoCreateReq("");
        }
        Todo todo = new Todo(null, todoCreateReq.content(), false, LocalDateTime.now(), LocalDateTime.now(), userId);
        todoRepository.save(todo);
        return TodoDetail.from(todo);
    }
}

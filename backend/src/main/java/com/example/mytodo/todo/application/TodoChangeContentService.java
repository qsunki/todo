package com.example.mytodo.todo.application;

import com.example.mytodo.todo.application.command.TodoChangeContentReq;
import com.example.mytodo.todo.application.exception.AccessDeniedException;
import com.example.mytodo.todo.domain.Todo;
import com.example.mytodo.todo.domain.TodoRepository;
import java.time.LocalDateTime;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoChangeContentService {

    private final TodoRepository todoRepository;

    public TodoChangeContentService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public TodoDetail changeContent(@NonNull TodoChangeContentReq todoChangeContentReq, @NonNull Long userId) {
        if (todoChangeContentReq.id() == null) {
            throw new IllegalArgumentException("todo id cannot be null");
        }
        Todo todo = todoRepository.findById(todoChangeContentReq.id()).orElseThrow();
        if (!userId.equals(todo.getUserId())) {
            throw new AccessDeniedException("userId does not match");
        }
        todo.changeContent(todoChangeContentReq.content(), LocalDateTime.now());
        Todo save = todoRepository.save(todo);
        return TodoDetail.from(save);
    }
}

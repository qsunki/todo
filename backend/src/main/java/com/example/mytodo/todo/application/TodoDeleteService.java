package com.example.mytodo.todo.application;

import com.example.mytodo.todo.application.command.TodoDeleteReq;
import com.example.mytodo.todo.application.exception.AccessDeniedException;
import com.example.mytodo.todo.domain.Todo;
import com.example.mytodo.todo.domain.TodoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoDeleteService {

    private final TodoRepository todoRepository;

    public TodoDeleteService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public void delete(@NonNull TodoDeleteReq todoDeleteReq, @NonNull Long userId) {
        if (todoDeleteReq.id() == null) {
            throw new IllegalArgumentException("todo id cannot be null");
        }
        Todo todo = todoRepository.findById(todoDeleteReq.id()).orElseThrow();
        if (!userId.equals(todo.getUserId())) {
            throw new AccessDeniedException("userId does not match");
        }
        todoRepository.deleteById(todo.getId());
    }
}

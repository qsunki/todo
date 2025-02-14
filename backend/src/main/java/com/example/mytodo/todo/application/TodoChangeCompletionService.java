package com.example.mytodo.todo.application;

import com.example.mytodo.todo.application.command.TodoChangeCompletionReq;
import com.example.mytodo.todo.application.exception.AccessDeniedException;
import com.example.mytodo.todo.domain.Todo;
import com.example.mytodo.todo.domain.TodoRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoChangeCompletionService {

    private final TodoRepository todoRepository;

    public TodoChangeCompletionService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public TodoDetail changeCompletion(TodoChangeCompletionReq todoChangeCompletionReq, Long userId) {
        if (todoChangeCompletionReq.id() == null) {
            throw new IllegalArgumentException("todo id cannot be null");
        }
        if (todoChangeCompletionReq.complete() == null) {
            throw new IllegalArgumentException("complete cannot be null");
        }
        Todo todo = todoRepository.findById(todoChangeCompletionReq.id()).orElseThrow();
        if (!userId.equals(todo.getUserId())) {
            throw new AccessDeniedException("userId does not match");
        }
        todo.changeComplete(todoChangeCompletionReq.complete(), LocalDateTime.now());
        Todo save = todoRepository.save(todo);
        return TodoDetail.from(save);
    }
}

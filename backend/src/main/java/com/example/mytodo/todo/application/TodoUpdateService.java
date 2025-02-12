package com.example.mytodo.todo.application;

import com.example.mytodo.todo.application.dto.TodoDetail;
import com.example.mytodo.todo.application.dto.TodoUpdateReq;
import com.example.mytodo.todo.application.exception.AccessDeniedException;
import com.example.mytodo.todo.domain.Todo;
import com.example.mytodo.todo.domain.TodoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoUpdateService {

    private final TodoRepository todoRepository;

    public TodoUpdateService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public TodoDetail update(@NonNull TodoUpdateReq todoUpdateReq, @NonNull Long userId) {
        if (todoUpdateReq.id() == null) {
            throw new IllegalArgumentException("todo id cannot be null");
        }
        Todo todo = todoRepository.findById(todoUpdateReq.id()).orElseThrow();
        if (!userId.equals(todo.getUserId())) {
            throw new AccessDeniedException("userId does not match");
        }
        todo.changeContent(todoUpdateReq.content());
        Todo save = todoRepository.save(todo);
        return TodoDetail.from(save);
    }
}

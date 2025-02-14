package com.example.mytodo.web;

import com.example.mytodo.infra.security.UserSessionInfo;
import com.example.mytodo.todo.application.*;
import com.example.mytodo.todo.application.command.TodoCreateReq;
import com.example.mytodo.todo.application.command.TodoDeleteReq;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
class TodoController {

    private final TodoCreateService todoCreateService;
    private final TodoDeleteService todoDeleteService;

    public TodoController(TodoCreateService todoCreateService, TodoDeleteService todoDeleteService) {
        this.todoCreateService = todoCreateService;
        this.todoDeleteService = todoDeleteService;
    }

    @PostMapping("/api/todos")
    TodoDetail create(
            @RequestBody TodoCreateReq todoCreateReq, @AuthenticationPrincipal UserSessionInfo userSessionInfo) {
        return todoCreateService.create(todoCreateReq, userSessionInfo.userId());
    }

    @DeleteMapping("/api/todos/{todoId}")
    void delete(@PathVariable Long todoId, @AuthenticationPrincipal UserSessionInfo userSessionInfo) {
        todoDeleteService.delete(new TodoDeleteReq(todoId), userSessionInfo.userId());
    }
}

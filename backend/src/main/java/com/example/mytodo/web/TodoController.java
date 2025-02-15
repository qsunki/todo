package com.example.mytodo.web;

import com.example.mytodo.infra.mybatis.TodoDao;
import com.example.mytodo.infra.security.UserSessionInfo;
import com.example.mytodo.todo.application.*;
import com.example.mytodo.todo.application.command.*;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
class TodoController {

    private final TodoCreateService todoCreateService;
    private final TodoChangeContentService todoChangeContentService;
    private final TodoChangeCompletionService todoChangeCompletionService;
    private final TodoDeleteService todoDeleteService;
    private final TodoDao todoDao;

    TodoController(
            TodoCreateService todoCreateService,
            TodoChangeContentService todoChangeContentService,
            TodoChangeCompletionService todoChangeCompletionService,
            TodoDeleteService todoDeleteService,
            TodoDao todoDao) {
        this.todoCreateService = todoCreateService;
        this.todoChangeContentService = todoChangeContentService;
        this.todoChangeCompletionService = todoChangeCompletionService;
        this.todoDeleteService = todoDeleteService;
        this.todoDao = todoDao;
    }

    @PostMapping("/api/todos")
    TodoDetail create(
            @RequestBody TodoCreateReq todoCreateReq, @AuthenticationPrincipal UserSessionInfo userSessionInfo) {
        return todoCreateService.create(todoCreateReq, userSessionInfo.userId());
    }

    @PatchMapping("/api/todos/{todoId}")
    TodoDetail changeContent(
            @PathVariable Long todoId,
            @RequestBody TodoUpdateReq todoUpdateReq,
            @AuthenticationPrincipal UserSessionInfo userSessionInfo) {
        return switch (todoUpdateReq.type()) {
            case "content" -> todoChangeContentService.changeContent(
                    new TodoChangeContentReq(todoId, todoUpdateReq.content()), userSessionInfo.userId());
            case "complete" -> todoChangeCompletionService.changeCompletion(
                    new TodoChangeCompletionReq(todoId, todoUpdateReq.complete()), userSessionInfo.userId());
            default -> throw new IllegalStateException("Unexpected value: " + todoUpdateReq);
        };
    }

    @DeleteMapping("/api/todos/{todoId}")
    void delete(@PathVariable Long todoId, @AuthenticationPrincipal UserSessionInfo userSessionInfo) {
        todoDeleteService.delete(new TodoDeleteReq(todoId), userSessionInfo.userId());
    }

    @PostMapping("/api/todos/query")
    List<TodoView> retrieve(
            @RequestBody TodoListRetrieveReq todoListRetrieveReq,
            @AuthenticationPrincipal UserSessionInfo userSessionInfo) {
        return todoDao.findAll(todoListRetrieveReq, userSessionInfo.userId());
    }
}

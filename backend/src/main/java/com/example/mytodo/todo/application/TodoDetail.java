package com.example.mytodo.todo.application;

import com.example.mytodo.todo.domain.Todo;

public record TodoDetail(Long id, String content) {
    public static TodoDetail from(Todo todo) {
        return new TodoDetail(todo.getId(), todo.getContent());
    }
}

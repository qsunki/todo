package com.example.mytodo.todo.domain;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("todos")
public class Todo {
    @Id
    private Long id;

    private String content;
    private boolean completed;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    private Long userId;

    public Todo(
            Long id,
            String content,
            boolean completed,
            LocalDateTime createdTime,
            LocalDateTime lastModifiedTime,
            Long userId) {
        this.id = id;
        this.content = content;
        this.completed = completed;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void changeContent(String content) {
        this.content = content == null ? "" : content;
    }
}

package com.example.mytodo.web;

import java.time.LocalDateTime;
import java.util.List;

public class TodoView {
    private Long id;
    private String content;
    private Boolean completed;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    private List<PropertyView> properties;

    public TodoView() {}

    public TodoView(
            Long id,
            String content,
            Boolean completed,
            LocalDateTime createdTime,
            LocalDateTime lastModifiedTime,
            List<PropertyView> properties) {
        this.id = id;
        this.content = content;
        this.completed = completed;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public List<PropertyView> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyView> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "TodoView{" + "id=" + id + ", content='" + content + '\'' + ", completed=" + completed + ", createdTime="
                + createdTime + ", lastModifiedTime=" + lastModifiedTime + ", properties=" + properties + '}';
    }
}

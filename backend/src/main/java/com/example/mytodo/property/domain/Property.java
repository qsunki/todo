package com.example.mytodo.property.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("properties")
public class Property {
    @Id
    Long id;

    private String name;
    private PropertyType type;
    private PropertyData data;
    private Long todoId;

    public Property(Long id, String name, PropertyType type, PropertyData data, Long todoId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.data = data;
        this.todoId = todoId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PropertyType getType() {
        return type;
    }

    public PropertyData getData() {
        return data;
    }

    public Long getTodoId() {
        return todoId;
    }
}

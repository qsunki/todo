package com.example.mytodo.property.application.command;

import com.example.mytodo.property.domain.PropertyData;
import com.example.mytodo.property.domain.PropertyType;

public record PropertyCreateReq(Long todoId, String name, PropertyType type, PropertyData data) {
    public PropertyCreateReq setTodoId(Long todoId) {
        return new PropertyCreateReq(todoId, name, type, data);
    }
}

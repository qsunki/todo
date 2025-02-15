package com.example.mytodo.property.application;

import com.example.mytodo.property.domain.Property;
import com.example.mytodo.property.domain.PropertyData;
import com.example.mytodo.property.domain.PropertyType;

public record PropertyDetail(Long id, String name, PropertyType type, PropertyData data, Long todoId) {
    public static PropertyDetail from(Property save) {
        return new PropertyDetail(save.getId(), save.getName(), save.getType(), save.getData(), save.getTodoId());
    }
}

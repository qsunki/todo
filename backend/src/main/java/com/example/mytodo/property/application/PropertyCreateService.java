package com.example.mytodo.property.application;

import com.example.mytodo.property.application.command.PropertyCreateReq;
import com.example.mytodo.property.domain.DateProperty;
import com.example.mytodo.property.domain.Property;
import com.example.mytodo.property.domain.PropertyRepository;
import com.example.mytodo.property.domain.SelectProperty;
import com.example.mytodo.todo.application.exception.AccessDeniedException;
import com.example.mytodo.todo.domain.Todo;
import com.example.mytodo.todo.domain.TodoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PropertyCreateService {
    private final PropertyRepository propertyRepository;
    private final TodoRepository todoRepository;

    public PropertyCreateService(PropertyRepository propertyRepository, TodoRepository todoRepository) {
        this.propertyRepository = propertyRepository;
        this.todoRepository = todoRepository;
    }

    public PropertyDetail create(@NonNull PropertyCreateReq propertyCreateReq, @NonNull Long userId) {
        if (!StringUtils.hasText(propertyCreateReq.name())) {
            throw new IllegalArgumentException("Property name cannot be empty");
        }
        if (propertyCreateReq.data() instanceof DateProperty dateProperty && dateProperty.getStart() == null) {
            throw new IllegalArgumentException("Start date cannot be empty");
        }
        if (propertyCreateReq.data() instanceof SelectProperty selectProperty
                && !StringUtils.hasText(selectProperty.getName())) {
            throw new IllegalArgumentException("SelectProperty name cannot be empty");
        }

        Todo todo = todoRepository.findById(propertyCreateReq.todoId()).orElseThrow();
        if (!userId.equals(todo.getUserId())) {
            throw new AccessDeniedException("userId does not match");
        }
        Property save = propertyRepository.save(new Property(
                null,
                propertyCreateReq.name(),
                propertyCreateReq.type(),
                propertyCreateReq.data(),
                propertyCreateReq.todoId()));
        return PropertyDetail.from(save);
    }
}

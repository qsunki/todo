package com.example.mytodo.web;

import com.example.mytodo.infra.security.UserSessionInfo;
import com.example.mytodo.property.application.PropertyCreateService;
import com.example.mytodo.property.application.PropertyDetail;
import com.example.mytodo.property.application.command.PropertyCreateReq;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PropertyController {

    private final PropertyCreateService propertyCreateService;

    public PropertyController(PropertyCreateService propertyCreateService) {
        this.propertyCreateService = propertyCreateService;
    }

    @PostMapping("/api/todos/{todoId}/properties")
    PropertyDetail create(
            @RequestBody PropertyCreateReq propertyCreateReq,
            @PathVariable Long todoId,
            @AuthenticationPrincipal UserSessionInfo userSessionInfo) {
        return propertyCreateService.create(propertyCreateReq.setTodoId(todoId), userSessionInfo.userId());
    }
}

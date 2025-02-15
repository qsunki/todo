package com.example.mytodo.web;

import com.example.mytodo.property.domain.PropertyData;
import com.example.mytodo.property.domain.PropertyType;
import java.util.List;

public record TodoListRetrieveReq(List<Filter> filters) {

    public record Filter(String name, PropertyType type, PropertyData data) {}
}

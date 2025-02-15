package com.example.mytodo.property.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DateProperty.class, name = "date"),
    @JsonSubTypes.Type(value = SelectProperty.class, name = "select")
})
public abstract class PropertyData {}

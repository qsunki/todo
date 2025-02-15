package com.example.mytodo.property.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)
public enum PropertyType {
    SELECT,
    DATE
}

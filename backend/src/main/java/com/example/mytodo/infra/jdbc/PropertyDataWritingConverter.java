package com.example.mytodo.infra.jdbc;

import com.example.mytodo.property.domain.PropertyData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class PropertyDataWritingConverter implements Converter<PropertyData, String> {
    private final ObjectMapper objectMapper;

    public PropertyDataWritingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convert(PropertyData source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert PropertyData to JSON", e);
        }
    }
}

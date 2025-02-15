package com.example.mytodo.infra.jdbc;

import com.example.mytodo.property.domain.PropertyData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class PropertyDataReadingConverter implements Converter<String, PropertyData> {
    private final ObjectMapper objectMapper;

    public PropertyDataReadingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public PropertyData convert(String source) {
        try {
            return objectMapper.readValue(source, PropertyData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to PropertyData", e);
        }
    }
}

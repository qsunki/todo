package com.example.mytodo.property.domain;

import java.util.Objects;

public class SelectProperty extends PropertyData {
    private String name;
    private String color;

    public SelectProperty() {}

    public SelectProperty(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SelectProperty that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}

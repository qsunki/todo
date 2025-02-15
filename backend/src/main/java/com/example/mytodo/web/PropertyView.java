package com.example.mytodo.web;

import com.fasterxml.jackson.annotation.JsonRawValue;

public class PropertyView {
    private Long id;
    private String name;
    private String type;

    @JsonRawValue
    private String data;

    public PropertyView(Long id, String name, String type, String data) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PropertyView{" + "id=" + id + ", name='" + name + '\'' + ", type='" + type + '\'' + ", data='" + data
                + '\'' + '}';
    }
}

package com.example.mytodo.property.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class DateProperty extends PropertyData {
    private LocalDateTime start;
    private LocalDateTime end;

    public DateProperty() {}

    public DateProperty(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DateProperty that)) return false;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}

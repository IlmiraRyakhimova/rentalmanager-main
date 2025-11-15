package com.rental.manager.entities.enums;

public enum BookingStatus {
    PENDING("Ожидает подтверждения"),
    CONFIRMED("Подтверждено"),
    CANCELLED("Отменено"),
    COMPLETED("Завершено");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

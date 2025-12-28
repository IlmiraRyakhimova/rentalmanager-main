package com.rental.manager.entities.enums;

public enum PaymentStatus {
    PENDING("Ожидает оплаты"),
    PREPAID("Внесен депозит"),
    PAID("Оплачено"),
    REFUNDED("Возврат");

    private String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

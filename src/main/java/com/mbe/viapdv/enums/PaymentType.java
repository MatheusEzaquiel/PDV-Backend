package com.mbe.viapdv.enums;

public enum PaymentType {
    CASH("Cash"),
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    PIX("PIX Payment"),
    BANK_TRANSFER("Bank Transfer"),
    NOT_INFORMED("Not Informed");

    private final String description;

    PaymentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}


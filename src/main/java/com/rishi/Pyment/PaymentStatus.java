package com.rishi.Pyment;

public enum PaymentStatus {
    PENDING("Payment is being processed"),
    SUCCESS("Payment completed successfully"),
    FAILED("Payment failed"),
    CANCELLED("Payment was cancelled"),
    REFUNDED("Payment was refunded"),
    PARTIALLY_REFUNDED("Payment was partially refunded"),
    EXPIRED("Payment link expired"),
    DECLINED("Payment was declined by bank/card"),
    PROCESSING("Payment is being processed by gateway"),
    AUTHORIZED("Payment is authorized but not captured"),
    CAPTURED("Payment is captured and completed"),
    VOIDED("Payment was voided");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSuccessful() {
        return this == SUCCESS || this == CAPTURED;
    }

    public boolean isFailed() {
        return this == FAILED || this == CANCELLED || this == DECLINED || this == EXPIRED;
    }

    public boolean isPending() {
        return this == PENDING || this == PROCESSING || this == AUTHORIZED;
    }
}

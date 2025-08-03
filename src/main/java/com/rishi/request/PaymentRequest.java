package com.rishi.request;

import com.rishi.Pyment.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    private Long userId;
    private Double amount;
    private String currency = "USD";
    private Payment.PaymentPurpose purpose;
    private String paymentMethod;
    private Long relatedEntityId;
    private String relatedEntityType;
    private String description;
    
    // Optional fields for specific payment types
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;
    private String cardholderName;
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingZipCode;
    private String billingCountry;
} 
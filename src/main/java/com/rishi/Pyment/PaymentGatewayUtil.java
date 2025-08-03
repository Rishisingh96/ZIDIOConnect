package com.rishi.Pyment;

import com.rishi.response.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class PaymentGatewayUtil {

    // Generate unique transaction ID
    public String generateTransactionId() {
        return "TXN_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    // Validate payment amount
    public boolean isValidAmount(Double amount) {
        return amount != null && amount > 0;
    }

    // Validate payment method
    public boolean isValidPaymentMethod(String paymentMethod) {
        if (paymentMethod == null) return false;
        
        return paymentMethod.equalsIgnoreCase("CREDIT_CARD") ||
               paymentMethod.equalsIgnoreCase("DEBIT_CARD") ||
               paymentMethod.equalsIgnoreCase("BANK_TRANSFER") ||
               paymentMethod.equalsIgnoreCase("PAYPAL") ||
               paymentMethod.equalsIgnoreCase("STRIPE") ||
               paymentMethod.equalsIgnoreCase("RAZORPAY");
    }

    // Process payment through Stripe (mock implementation)
    public PaymentResponse processStripePayment(String transactionId, Double amount, String currency, String cardToken) {
        try {
            log.info("Processing Stripe payment for transaction: {}", transactionId);
            
            // Mock Stripe API call
            String gatewayTransactionId = "stripe_" + System.currentTimeMillis();
            
            // Simulate payment processing
            if (Math.random() > 0.1) { // 90% success rate
                return new PaymentResponse(transactionId, PaymentStatus.SUCCESS, 
                    "Payment processed successfully", gatewayTransactionId);
            } else {
                return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                    "Payment failed", "Insufficient funds", false);
            }
        } catch (Exception e) {
            log.error("Error processing Stripe payment: {}", e.getMessage());
            return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                "Payment processing error", e.getMessage(), false);
        }
    }

    // Process payment through PayPal (mock implementation)
    public PaymentResponse processPayPalPayment(String transactionId, Double amount, String currency) {
        try {
            log.info("Processing PayPal payment for transaction: {}", transactionId);
            
            // Mock PayPal API call
            String gatewayTransactionId = "paypal_" + System.currentTimeMillis();
            String paymentUrl = "https://www.paypal.com/pay/" + gatewayTransactionId;
            LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);
            
            return new PaymentResponse(transactionId, PaymentStatus.PENDING, 
                "Payment link generated", paymentUrl, expiresAt);
        } catch (Exception e) {
            log.error("Error processing PayPal payment: {}", e.getMessage());
            return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                "Payment processing error", e.getMessage(), false);
        }
    }

    // Process payment through Razorpay (mock implementation)
    public PaymentResponse processRazorpayPayment(String transactionId, Double amount, String currency) {
        try {
            log.info("Processing Razorpay payment for transaction: {}", transactionId);
            
            // Mock Razorpay API call
            String gatewayTransactionId = "razorpay_" + System.currentTimeMillis();
            String paymentUrl = "https://checkout.razorpay.com/pay/" + gatewayTransactionId;
            LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
            
            return new PaymentResponse(transactionId, PaymentStatus.PENDING, 
                "Payment link generated", paymentUrl, expiresAt);
        } catch (Exception e) {
            log.error("Error processing Razorpay payment: {}", e.getMessage());
            return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                "Payment processing error", e.getMessage(), false);
        }
    }

    // Process bank transfer (mock implementation)
    public PaymentResponse processBankTransfer(String transactionId, Double amount, String currency) {
        try {
            log.info("Processing bank transfer for transaction: {}", transactionId);
            
            // Mock bank transfer processing
            String gatewayTransactionId = "bank_" + System.currentTimeMillis();
            
            // Bank transfers are typically pending initially
            return new PaymentResponse(transactionId, PaymentStatus.PENDING, 
                "Bank transfer initiated", gatewayTransactionId);
        } catch (Exception e) {
            log.error("Error processing bank transfer: {}", e.getMessage());
            return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                "Bank transfer error", e.getMessage(), false);
        }
    }

    // Verify payment status with gateway
    public PaymentStatus verifyPaymentStatus(String gatewayTransactionId, String paymentMethod) {
        try {
            log.info("Verifying payment status for gateway transaction: {}", gatewayTransactionId);
            
            // Mock verification based on payment method
            switch (paymentMethod.toUpperCase()) {
                case "STRIPE":
                    return Math.random() > 0.1 ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
                case "PAYPAL":
                    return Math.random() > 0.05 ? PaymentStatus.SUCCESS : PaymentStatus.PENDING;
                case "RAZORPAY":
                    return Math.random() > 0.1 ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
                case "BANK_TRANSFER":
                    return Math.random() > 0.2 ? PaymentStatus.SUCCESS : PaymentStatus.PENDING;
                default:
                    return PaymentStatus.FAILED;
            }
        } catch (Exception e) {
            log.error("Error verifying payment status: {}", e.getMessage());
            return PaymentStatus.FAILED;
        }
    }

    // Refund payment
    public PaymentResponse refundPayment(String transactionId, Double amount, String reason) {
        try {
            log.info("Processing refund for transaction: {}", transactionId);
            
            String gatewayTransactionId = "refund_" + System.currentTimeMillis();
            
            // Mock refund processing
            if (Math.random() > 0.05) { // 95% success rate for refunds
                return new PaymentResponse(transactionId, PaymentStatus.REFUNDED, 
                    "Refund processed successfully", gatewayTransactionId);
            } else {
                return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                    "Refund failed", "Gateway error", false);
            }
        } catch (Exception e) {
            log.error("Error processing refund: {}", e.getMessage());
            return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                "Refund processing error", e.getMessage(), false);
        }
    }

    // Get payment gateway configuration
    public String getGatewayConfig(String paymentMethod) {
        switch (paymentMethod.toUpperCase()) {
            case "STRIPE":
                return "stripe_config";
            case "PAYPAL":
                return "paypal_config";
            case "RAZORPAY":
                return "razorpay_config";
            case "BANK_TRANSFER":
                return "bank_transfer_config";
            default:
                return "default_config";
        }
    }
}

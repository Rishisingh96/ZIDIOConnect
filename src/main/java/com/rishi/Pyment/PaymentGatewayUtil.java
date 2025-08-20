package com.rishi.Pyment;

import java.time.LocalDateTime;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.rishi.response.PaymentResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentGatewayUtil {

    // Razorpay credentials
    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Value("${razorpay.currency:INR}")
    private String razorpayCurrency;

    // Generate unique transaction ID
    public String generateTransactionId() {
        return "TXN_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    // Validate payment amount
    public boolean isValidAmount(Double amount) {
        return amount != null && amount > 0;
    }

    // Validate payment method (only Razorpay supported)
    public boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod != null && "RAZORPAY".equalsIgnoreCase(paymentMethod);
    }

    // Process payment through Razorpay: creates Order and returns orderId in gatewayTransactionId
    public PaymentResponse processRazorpayPayment(String transactionId, Double amount, String currency) {
        try {
            log.info("Processing Razorpay payment for transaction: {}", transactionId);
            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", Math.round(amount * 100));
            orderRequest.put("currency", currency == null ? razorpayCurrency : currency.toUpperCase());
            orderRequest.put("receipt", transactionId);
            orderRequest.put("payment_capture", 1);

            Order order = client.orders.create(orderRequest);
            String orderId = order.get("id");
            String paymentUrl = "https://checkout.razorpay.com/v1/checkout.js?order_id=" + orderId;

            PaymentResponse resp = new PaymentResponse(transactionId, PaymentStatus.PENDING,
                    "Razorpay order created", paymentUrl, LocalDateTime.now().plusHours(1));
            resp.setGatewayTransactionId(orderId);
            return resp;
        } catch (Exception e) {
            log.error("Error processing Razorpay payment: {}", e.getMessage());
            return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                "Payment processing error", e.getMessage(), false);
        }
    }

    // Verify payment status with Razorpay
    public PaymentStatus verifyPaymentStatus(String gatewayTransactionId, String paymentMethod) {
        try {
            log.info("Verifying payment status for gateway transaction: {}", gatewayTransactionId);
            
            // For Razorpay, you would typically verify the payment signature here
            // This is a simplified implementation - in production, verify the signature
            if ("RAZORPAY".equalsIgnoreCase(paymentMethod)) {
                // Mock verification - replace with actual Razorpay signature verification
                return Math.random() > 0.1 ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
            }
            
            return PaymentStatus.FAILED;
        } catch (Exception e) {
            log.error("Error verifying payment status: {}", e.getMessage());
            return PaymentStatus.FAILED;
        }
    }

    // Refund payment through Razorpay
    public PaymentResponse refundPayment(String transactionId, Double amount, String reason) {
        try {
            log.info("Processing refund for transaction: {}", transactionId);
            
            // In production, implement actual Razorpay refund API call
            String gatewayTransactionId = "refund_" + System.currentTimeMillis();
            
            // Mock refund processing - replace with actual Razorpay refund API
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
}

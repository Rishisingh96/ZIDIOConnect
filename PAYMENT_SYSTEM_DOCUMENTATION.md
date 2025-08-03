# Zidio Connect Payment System Documentation

## Overview

The Payment System provides comprehensive payment processing capabilities for the Zidio Connect job portal application. It supports multiple payment gateways, job portal specific payment types, and complete payment lifecycle management.

## Base URL

```
http://localhost:8080/api/v1/payments
```

## Authentication

All endpoints require authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Payment Statuses

```json
{
  "PENDING": "Payment is being processed",
  "SUCCESS": "Payment completed successfully",
  "FAILED": "Payment failed",
  "CANCELLED": "Payment was cancelled",
  "REFUNDED": "Payment was refunded",
  "PARTIALLY_REFUNDED": "Payment was partially refunded",
  "EXPIRED": "Payment link expired",
  "DECLINED": "Payment was declined by bank/card",
  "PROCESSING": "Payment is being processed by gateway",
  "AUTHORIZED": "Payment is authorized but not captured",
  "CAPTURED": "Payment is captured and completed",
  "VOIDED": "Payment was voided"
}
```

## Payment Purposes (Job Portal Specific)

```json
{
  "JOB_POSTING": "Job Posting",
  "PREMIUM_JOB_POSTING": "Premium Job Posting",
  "FEATURED_PROFILE": "Featured Profile",
  "RESUME_ACCESS": "Resume Access",
  "INTERVIEW_SCHEDULING": "Interview Scheduling",
  "RECRUITER_VERIFICATION": "Recruiter Verification",
  "PREMIUM_SUBSCRIPTION": "Premium Subscription",
  "ADVERTISING": "Advertising",
  "CUSTOM_SERVICE": "Custom Service"
}
```

## API Endpoints

### 1. Process Payment

**POST** `/process`

**Request Body:**

```json
{
  "userId": 123,
  "amount": 99.99,
  "currency": "USD",
  "purpose": "JOB_POSTING",
  "paymentMethod": "STRIPE",
  "relatedEntityId": 456,
  "relatedEntityType": "JOB",
  "description": "Payment for job posting: Senior Java Developer",
  "cardNumber": "4242424242424242",
  "expiryMonth": "12",
  "expiryYear": "2025",
  "cvv": "123",
  "cardholderName": "John Doe",
  "billingAddress": "123 Main St",
  "billingCity": "New York",
  "billingState": "NY",
  "billingZipCode": "10001",
  "billingCountry": "USA"
}
```

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "SUCCESS",
  "message": "Payment processed successfully",
  "gatewayTransactionId": "stripe_1703123456789",
  "success": true
}
```

### 2. Process Card Payment

**POST** `/process/card`

**Request Body:**

```json
{
  "userId": 123,
  "amount": 99.99,
  "currency": "USD",
  "purpose": "PREMIUM_JOB_POSTING",
  "paymentMethod": "CREDIT_CARD",
  "cardNumber": "4242424242424242",
  "expiryMonth": "12",
  "expiryYear": "2025",
  "cvv": "123",
  "cardholderName": "John Doe"
}
```

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "SUCCESS",
  "message": "Payment processed successfully",
  "gatewayTransactionId": "stripe_1703123456789",
  "success": true
}
```

### 3. Process PayPal Payment

**POST** `/process/paypal`

**Request Body:**

```json
{
  "userId": 123,
  "amount": 99.99,
  "currency": "USD",
  "purpose": "FEATURED_PROFILE",
  "paymentMethod": "PAYPAL"
}
```

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "PENDING",
  "message": "Payment link generated",
  "paymentUrl": "https://www.paypal.com/pay/paypal_1703123456789",
  "expiresAt": "2024-01-16T10:30:00",
  "success": false
}
```

### 4. Get Payment by ID

**GET** `/payments/{paymentId}`

**Response:**

```json
{
  "id": 1,
  "userId": 123,
  "transactionId": "TXN_1703123456789_abc12345",
  "amount": 99.99,
  "currency": "USD",
  "status": "SUCCESS",
  "purpose": "JOB_POSTING",
  "paymentMethod": "STRIPE",
  "gatewayResponse": "Payment processed successfully",
  "gatewayTransactionId": "stripe_1703123456789",
  "paymentDate": "2024-01-15T10:30:00",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "relatedEntityId": 456,
  "relatedEntityType": "JOB",
  "description": "Payment for job posting: Senior Java Developer",
  "failureReason": null
}
```

### 5. Get User Payments

**GET** `/user/{userId}?page=0&size=10&sortBy=createdAt&sortDir=desc`

**Response:**

```json
{
  "content": [
    {
      "id": 1,
      "userId": 123,
      "transactionId": "TXN_1703123456789_abc12345",
      "amount": 99.99,
      "currency": "USD",
      "status": "SUCCESS",
      "purpose": "JOB_POSTING",
      "paymentMethod": "STRIPE",
      "gatewayResponse": "Payment processed successfully",
      "gatewayTransactionId": "stripe_1703123456789",
      "paymentDate": "2024-01-15T10:30:00",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00",
      "relatedEntityId": 456,
      "relatedEntityType": "JOB",
      "description": "Payment for job posting: Senior Java Developer",
      "failureReason": null
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 25,
  "totalPages": 3,
  "lastPage": false
}
```

### 6. Verify Payment Status

**PUT** `/verify/{transactionId}`

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "SUCCESS",
  "message": "Payment status verified",
  "gatewayTransactionId": "stripe_1703123456789",
  "success": true
}
```

### 7. Cancel Payment

**PUT** `/cancel/{transactionId}`

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "CANCELLED",
  "message": "Payment status updated",
  "gatewayTransactionId": "stripe_1703123456789",
  "success": false
}
```

### 8. Refund Payment

**POST** `/refund/{transactionId}?amount=99.99&reason=Customer request`

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "REFUNDED",
  "message": "Refund processed successfully",
  "gatewayTransactionId": "refund_1703123456789",
  "success": true
}
```

## Job Portal Specific Payment Endpoints

### 9. Job Posting Payment

**POST** `/job-posting?userId=123&jobId=456&jobTitle=Senior Java Developer&amount=99.99`

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "SUCCESS",
  "message": "Payment processed successfully",
  "gatewayTransactionId": "stripe_1703123456789",
  "success": true
}
```

### 10. Premium Job Posting Payment

**POST** `/premium-job-posting?userId=123&jobId=456&jobTitle=Senior Java Developer&amount=199.99`

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "SUCCESS",
  "message": "Payment processed successfully",
  "gatewayTransactionId": "stripe_1703123456789",
  "success": true
}
```

### 11. Featured Profile Payment

**POST** `/featured-profile?userId=123&profileId=789&amount=49.99`

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "SUCCESS",
  "message": "Payment processed successfully",
  "gatewayTransactionId": "stripe_1703123456789",
  "success": true
}
```

### 12. Resume Access Payment

**POST** `/resume-access?userId=123&resumeId=789&amount=29.99`

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "SUCCESS",
  "message": "Payment processed successfully",
  "gatewayTransactionId": "stripe_1703123456789",
  "success": true
}
```

### 13. Interview Scheduling Payment

**POST** `/interview-scheduling?userId=123&applicationId=789&amount=19.99`

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "SUCCESS",
  "message": "Payment processed successfully",
  "gatewayTransactionId": "stripe_1703123456789",
  "success": true
}
```

### 14. Recruiter Verification Payment

**POST** `/recruiter-verification?userId=123&amount=99.99`

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "SUCCESS",
  "message": "Payment processed successfully",
  "gatewayTransactionId": "stripe_1703123456789",
  "success": true
}
```

### 15. Premium Subscription Payment

**POST** `/premium-subscription?userId=123&subscriptionType=MONTHLY&amount=29.99`

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "SUCCESS",
  "message": "Payment processed successfully",
  "gatewayTransactionId": "stripe_1703123456789",
  "success": true
}
```

### 16. Advertising Payment

**POST** `/advertising?userId=123&adType=BANNER&amount=199.99`

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "SUCCESS",
  "message": "Payment processed successfully",
  "gatewayTransactionId": "stripe_1703123456789",
  "success": true
}
```

## Analytics and Reporting Endpoints

### 17. Get Revenue by Date Range

**GET** `/revenue/date-range?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59`

**Response:**

```json
15499.99
```

### 18. Get Revenue by User

**GET** `/revenue/user/{userId}`

**Response:**

```json
599.99
```

### 19. Get Successful Payment Count by User

**GET** `/successful-count/user/{userId}`

**Response:**

```json
15
```

### 20. Get Recent Payments

**GET** `/recent/{days}`

**Response:**

```json
[
  {
    "id": 1,
    "userId": 123,
    "transactionId": "TXN_1703123456789_abc12345",
    "amount": 99.99,
    "currency": "USD",
    "status": "SUCCESS",
    "purpose": "JOB_POSTING",
    "paymentMethod": "STRIPE",
    "gatewayResponse": "Payment processed successfully",
    "gatewayTransactionId": "stripe_1703123456789",
    "paymentDate": "2024-01-15T10:30:00",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00",
    "relatedEntityId": 456,
    "relatedEntityType": "JOB",
    "description": "Payment for job posting: Senior Java Developer",
    "failureReason": null
  }
]
```

## Payment Validation Endpoints

### 21. Check if Payment is Successful

**GET** `/validate/{transactionId}/successful`

**Response:**

```json
true
```

### 22. Check if Payment is Pending

**GET** `/validate/{transactionId}/pending`

**Response:**

```json
false
```

### 23. Check if Payment Failed

**GET** `/validate/{transactionId}/failed`

**Response:**

```json
false
```

## Admin Management Endpoints

### 24. Cleanup Expired Payments

**POST** `/cleanup/expired`

**Response:**

```json
{
  "message": "Expired payments cleaned up successfully",
  "role": null,
  "success": true,
  "status": "OK"
}
```

### 25. Cleanup Failed Payments

**POST** `/cleanup/failed?daysOld=30`

**Response:**

```json
{
  "message": "Failed payments cleaned up successfully",
  "role": null,
  "success": true,
  "status": "OK"
}
```

### 26. Archive Old Payments

**POST** `/archive?daysOld=365`

**Response:**

```json
{
  "message": "Old payments archived successfully",
  "role": null,
  "success": true,
  "status": "OK"
}
```

## Payment Webhook Handling

### 27. Handle Payment Webhook

**POST** `/webhook?gatewayTransactionId=stripe_1703123456789&status=SUCCESS&response=Payment processed successfully`

**Response:**

```json
{
  "transactionId": "TXN_1703123456789_abc12345",
  "status": "SUCCESS",
  "message": "Webhook processed successfully",
  "gatewayTransactionId": "stripe_1703123456789",
  "success": true
}
```

## Payment Method Management

### 28. Get Available Payment Methods

**GET** `/methods/available`

**Response:**

```json
["CREDIT_CARD", "DEBIT_CARD", "PAYPAL", "RAZORPAY", "BANK_TRANSFER"]
```

### 29. Check if Payment Method is Enabled

**GET** `/methods/{method}/enabled`

**Response:**

```json
true
```

### 30. Update Payment Method Settings

**PUT** `/methods/{method}/settings?enabled=true`

**Response:**

```json
{
  "message": "Payment method settings updated successfully",
  "role": null,
  "success": true,
  "status": "OK"
}
```

## Usage Examples

### Example 1: Job Posting Payment Flow

```bash
# 1. Recruiter creates a job posting
POST /api/v1/jobs
{
  "title": "Senior Java Developer",
  "description": "We are looking for a senior Java developer...",
  "company": "Tech Corp",
  "location": "New York"
}

# 2. Process payment for job posting
POST /api/v1/payments/job-posting?userId=123&jobId=456&jobTitle=Senior Java Developer&amount=99.99

# 3. Verify payment status
PUT /api/v1/payments/verify/TXN_1703123456789_abc12345

# 4. Activate job posting after successful payment
PUT /api/v1/jobs/456/activate
```

### Example 2: Premium Job Posting with Card Payment

```bash
# 1. Process premium job posting payment with card
POST /api/v1/payments/process/card
{
  "userId": 123,
  "amount": 199.99,
  "currency": "USD",
  "purpose": "PREMIUM_JOB_POSTING",
  "paymentMethod": "CREDIT_CARD",
  "relatedEntityId": 456,
  "relatedEntityType": "JOB",
  "description": "Payment for premium job posting: Senior Java Developer",
  "cardNumber": "4242424242424242",
  "expiryMonth": "12",
  "expiryYear": "2025",
  "cvv": "123",
  "cardholderName": "John Doe"
}

# 2. Check payment status
GET /api/v1/payments/validate/TXN_1703123456789_abc12345/successful
```

### Example 3: Featured Profile Payment with PayPal

```bash
# 1. Process featured profile payment with PayPal
POST /api/v1/payments/process/paypal
{
  "userId": 123,
  "amount": 49.99,
  "currency": "USD",
  "purpose": "FEATURED_PROFILE",
  "paymentMethod": "PAYPAL",
  "relatedEntityId": 789,
  "relatedEntityType": "PROFILE",
  "description": "Payment for featured profile"
}

# 2. Redirect user to PayPal
# User completes payment on PayPal

# 3. Handle PayPal webhook
POST /api/v1/payments/webhook?gatewayTransactionId=paypal_1703123456789&status=SUCCESS&response=Payment completed
```

### Example 4: Resume Access Payment

```bash
# 1. Recruiter pays to access candidate resume
POST /api/v1/payments/resume-access?userId=123&resumeId=789&amount=29.99

# 2. Verify payment and grant access
GET /api/v1/payments/validate/TXN_1703123456789_abc12345/successful

# 3. If successful, grant resume access
POST /api/v1/resumes/789/grant-access?userId=123
```

### Example 5: Interview Scheduling Payment

```bash
# 1. Process interview scheduling payment
POST /api/v1/payments/interview-scheduling?userId=123&applicationId=789&amount=19.99

# 2. After successful payment, schedule interview
POST /api/v1/interviews
{
  "applicationId": 789,
  "interviewDate": "2024-01-20T14:00:00",
  "interviewType": "VIDEO_CALL",
  "duration": 60
}
```

## Error Responses

### 400 - Bad Request

```json
{
  "message": "Invalid payment request",
  "role": null,
  "success": false,
  "status": "BAD_REQUEST"
}
```

### 404 - Payment Not Found

```json
{
  "message": "Payment not found with transaction id: TXN_INVALID",
  "role": null,
  "success": false,
  "status": "NOT_FOUND"
}
```

### 403 - Access Denied

```json
{
  "message": "Access Denied",
  "role": null,
  "success": false,
  "status": "FORBIDDEN"
}
```

### 500 - Payment Processing Error

```json
{
  "message": "Payment processing error",
  "role": null,
  "success": false,
  "status": "INTERNAL_SERVER_ERROR"
}
```

## Integration with Other Services

The payment system integrates with other services in your application:

1. **Job Service**: Activate job postings after successful payment
2. **Profile Service**: Feature profiles after payment
3. **Interview Service**: Schedule interviews after payment
4. **Notification Service**: Send payment confirmation notifications
5. **Email Service**: Send payment receipts
6. **WebSocket Service**: Real-time payment status updates

## Database Schema

The payment system uses the following database table:

```sql
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    transaction_id VARCHAR(255) UNIQUE NOT NULL,
    amount DOUBLE NOT NULL,
    currency VARCHAR(10) DEFAULT 'USD',
    status VARCHAR(50) NOT NULL,
    payment_purpose VARCHAR(50) NOT NULL,
    payment_method VARCHAR(50),
    gateway_response TEXT,
    gateway_transaction_id VARCHAR(255),
    payment_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    related_entity_id BIGINT,
    related_entity_type VARCHAR(50),
    description TEXT,
    failure_reason TEXT,
    INDEX idx_user_id (user_id),
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_status (status),
    INDEX idx_purpose (payment_purpose),
    INDEX idx_created_at (created_at)
);
```

## Best Practices

1. **Always validate payment requests** before processing
2. **Use webhooks** for payment status updates
3. **Implement proper error handling** for failed payments
4. **Store payment details securely** and comply with PCI DSS
5. **Use idempotency keys** to prevent duplicate payments
6. **Implement retry logic** for failed payment attempts
7. **Monitor payment success rates** and gateway performance
8. **Provide clear error messages** to users
9. **Implement payment timeouts** for pending payments
10. **Use proper logging** for payment debugging

## Security Considerations

1. **Never store sensitive card data** in your database
2. **Use HTTPS** for all payment endpoints
3. **Implement proper authentication** and authorization
4. **Validate payment amounts** on both client and server
5. **Use webhook signatures** to verify payment notifications
6. **Implement rate limiting** for payment endpoints
7. **Monitor for suspicious payment patterns**
8. **Comply with PCI DSS** if handling card data
9. **Use secure payment gateways** with proper certifications
10. **Implement proper audit logging** for all payment activities

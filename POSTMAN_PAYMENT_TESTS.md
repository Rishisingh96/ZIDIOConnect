# 🧪 Postman Tests for ZIDIOConnect Payment Service

Complete testing guide for all payment service endpoints using Postman.

## 🚀 Base URL

```
http://localhost:9090/api/v1/payments
```

## 📋 Test Collection Setup

### 1. Environment Variables

Create a new environment in Postman with these variables:

```
BASE_URL: http://localhost:9090
JWT_TOKEN: [Your JWT token after login]
USER_ID: 1
```

### 2. Headers

For authenticated endpoints, add this header:

```
Authorization: Bearer {{JWT_TOKEN}}
Content-Type: application/json
```

---

## 🧪 TEST ENDPOINTS (No Authentication Required)

### 1. Test Service Health

```
GET {{BASE_URL}}/api/v1/payments/test
```

**Expected Response:**

```json
"Payment Service is working! Razorpay configured successfully."
```

### 2. Get Sample Payment Request JSON

```
GET {{BASE_URL}}/api/v1/payments/test/sample-request
```

**Expected Response:**

```json
{
  "userId": 1,
  "amount": 1000.0,
  "currency": "INR",
  "purpose": "PREMIUM_JOB_POSTING",
  "paymentMethod": "RAZORPAY",
  "relatedEntityId": 456,
  "relatedEntityType": "JOB",
  "description": "Sample premium job posting payment"
}
```

### 3. Validate Payment Request

```
POST {{BASE_URL}}/api/v1/payments/test/validate-request
```

**Request Body:**

```json
{
  "userId": 1,
  "amount": 500.0,
  "currency": "INR",
  "purpose": "JOB_POSTING",
  "paymentMethod": "RAZORPAY",
  "relatedEntityId": 123,
  "relatedEntityType": "JOB",
  "description": "Test job posting payment"
}
```

**Expected Response:**

```json
"Payment request is VALID"
```

---

## 💳 CORE PAYMENT OPERATIONS

### 4. Process Payment (General)

```
POST {{BASE_URL}}/api/v1/payments/process
```

**Request Body:**

```json
{
  "userId": 1,
  "amount": 750.0,
  "currency": "INR",
  "purpose": "FEATURED_PROFILE",
  "paymentMethod": "RAZORPAY",
  "relatedEntityId": 789,
  "relatedEntityType": "PROFILE",
  "description": "Payment for featured profile promotion"
}
```

**Expected Response:**

```json
{
  "transactionId": "TXN_1234567890_abc123",
  "status": "PENDING",
  "message": "Razorpay order created",
  "gatewayTransactionId": "order_abc123",
  "paymentUrl": "https://checkout.razorpay.com/v1/checkout.js?order_id=order_abc123",
  "expiresAt": "2024-01-15T10:30:00",
  "success": false
}
```

### 5. Process Razorpay Payment (Direct)

```
POST {{BASE_URL}}/api/v1/payments/process/razorpay
```

**Request Body:**

```json
{
  "userId": 1,
  "amount": 1200.0,
  "currency": "INR",
  "purpose": "PREMIUM_SUBSCRIPTION",
  "paymentMethod": "RAZORPAY",
  "relatedEntityType": "SUBSCRIPTION",
  "description": "Premium subscription payment"
}
```

---

## 🔍 PAYMENT RETRIEVAL

### 6. Get Payment by ID

```
GET {{BASE_URL}}/api/v1/payments/1
```

### 7. Get Payment by Transaction ID

```
GET {{BASE_URL}}/api/v1/payments/transaction/TXN_1234567890_abc123
```

### 8. Get User Payments (Paginated)

```
GET {{BASE_URL}}/api/v1/payments/user/1?page=0&size=5&sortBy=createdAt&sortDir=desc
```

---

## 📊 PAYMENT STATUS MANAGEMENT

### 9. Verify Payment Status

```
PUT {{BASE_URL}}/api/v1/payments/verify/TXN_1234567890_abc123
```

### 10. Update Payment Status (Admin Only)

```
PUT {{BASE_URL}}/api/v1/payments/status/TXN_1234567890_abc123?status=SUCCESS
```

### 11. Razorpay Payment Verification

```
POST {{BASE_URL}}/api/v1/payments/razorpay/verify
```

**Query Params:**

```
transactionId: TXN_1234567890_abc123
razorpayPaymentId: pay_abc123
razorpayOrderId: order_abc123
razorpaySignature: signature_abc123
```

---

## 💸 REFUND OPERATIONS

### 12. Process Refund (Admin Only)

```
POST {{BASE_URL}}/api/v1/payments/refund/TXN_1234567890_abc123?amount=500&reason=Customer request
```

---

## 💼 JOB PORTAL SPECIFIC PAYMENTS

### 13. Job Posting Payment

```
POST {{BASE_URL}}/api/v1/payments/job-posting?userId=1&jobId=123&jobTitle=Software Engineer&amount=500
```

### 14. Premium Job Posting Payment

```
POST {{BASE_URL}}/api/v1/payments/premium-job-posting?userId=1&jobId=456&jobTitle=Senior Developer&amount=1000
```

### 15. Featured Profile Payment

```
POST {{BASE_URL}}/api/v1/payments/featured-profile?userId=1&profileId=789&amount=300
```

---

## ✅ PAYMENT VALIDATION

### 16. Check Payment Success

```
GET {{BASE_URL}}/api/v1/payments/validate/TXN_1234567890_abc123/successful
```

---

## 🔗 WEBHOOK HANDLING

### 17. Payment Webhook

```
POST {{BASE_URL}}/api/v1/payments/webhook
```

**Query Params:**

```
gatewayTransactionId: order_abc123
status: SUCCESS
response: {"payment_id": "pay_abc123", "status": "captured"}
```

---

## 🧪 COMPREHENSIVE TEST SCENARIOS

### Test Scenario 1: Complete Payment Flow

1. **Create Sample Payment** → `POST /test/create-sample`
2. **Get Payment Details** → `GET /{paymentId}`
3. **Verify Payment Status** → `PUT /verify/{transactionId}`
4. **Check if Successful** → `GET /validate/{transactionId}/successful`

### Test Scenario 2: Job Posting Payment Flow

1. **Process Job Posting Payment** → `POST /job-posting`
2. **Get User Payments** → `GET /user/{userId}`
3. **Update Status to Success** → `PUT /status/{transactionId}?status=SUCCESS`

### Test Scenario 3: Refund Flow

1. **Process Payment** → `POST /process`
2. **Update to Success** → `PUT /status/{transactionId}?status=SUCCESS`
3. **Process Refund** → `POST /refund/{transactionId}`

---

## 📝 TEST DATA EXAMPLES

### Valid Payment Request Examples

#### Job Posting Payment

```json
{
  "userId": 1,
  "amount": 500.0,
  "currency": "INR",
  "purpose": "JOB_POSTING",
  "paymentMethod": "RAZORPAY",
  "relatedEntityId": 123,
  "relatedEntityType": "JOB",
  "description": "Payment for job posting: Software Engineer"
}
```

#### Featured Profile Payment

```json
{
  "userId": 1,
  "amount": 300.0,
  "currency": "INR",
  "purpose": "FEATURED_PROFILE",
  "paymentMethod": "RAZORPAY",
  "relatedEntityId": 456,
  "relatedEntityType": "PROFILE",
  "description": "Payment for featured profile promotion"
}
```

#### Premium Subscription Payment

```json
{
  "userId": 1,
  "amount": 1200.0,
  "currency": "INR",
  "purpose": "PREMIUM_SUBSCRIPTION",
  "paymentMethod": "RAZORPAY",
  "relatedEntityType": "SUBSCRIPTION",
  "description": "Premium subscription for 6 months"
}
```

### Invalid Payment Request Examples

#### Missing Required Fields

```json
{
  "amount": 500.0,
  "currency": "INR"
}
```

#### Invalid Amount

```json
{
  "userId": 1,
  "amount": -100.0,
  "currency": "INR",
  "purpose": "JOB_POSTING",
  "paymentMethod": "RAZORPAY"
}
```

#### Invalid Payment Method

```json
{
  "userId": 1,
  "amount": 500.0,
  "currency": "INR",
  "purpose": "JOB_POSTING",
  "paymentMethod": "INVALID_METHOD"
}
```

---

## 🔍 EXPECTED RESPONSES

### Success Response

```json
{
  "transactionId": "TXN_1234567890_abc123",
  "status": "PENDING",
  "message": "Razorpay order created",
  "gatewayTransactionId": "order_abc123",
  "paymentUrl": "https://checkout.razorpay.com/v1/checkout.js?order_id=order_abc123",
  "expiresAt": "2024-01-15T10:30:00",
  "success": false
}
```

### Error Response

```json
{
  "transactionId": null,
  "status": "FAILED",
  "message": "Invalid payment request",
  "gatewayTransactionId": null,
  "paymentUrl": null,
  "expiresAt": null,
  "failureReason": "Validation failed",
  "success": false
}
```

---

## 🚨 ERROR HANDLING TESTS

### Test Invalid Scenarios

1. **Invalid User ID** → Use non-existent user ID
2. **Negative Amount** → Use negative amount values
3. **Invalid Currency** → Use unsupported currency
4. **Missing Required Fields** → Omit required fields
5. **Invalid Payment Purpose** → Use non-existent purpose
6. **Invalid Payment Method** → Use non-Razorpay method

---

## 📊 PERFORMANCE TESTS

### Load Testing

1. **Multiple Concurrent Payments** → Send 10+ payment requests simultaneously
2. **Large Amount Payments** → Test with very large amounts
3. **Rapid Status Updates** → Update payment status rapidly
4. **Bulk User Payments** → Get payments for multiple users

---

## 🔐 SECURITY TESTS

### Authentication Tests

1. **Missing JWT Token** → Remove Authorization header
2. **Invalid JWT Token** → Use expired or invalid token
3. **Wrong Role Access** → Test admin endpoints with user role

### Authorization Tests

1. **Access Other User's Payments** → Try to access payments of different user
2. **Admin-Only Operations** → Test admin endpoints with non-admin user

---

## 📱 POSTMAN COLLECTION IMPORT

### Collection JSON

```json
{
  "info": {
    "name": "ZIDIOConnect Payment Service Tests",
    "description": "Complete test collection for payment service endpoints"
  },
  "item": [
    {
      "name": "Test Endpoints",
      "item": [
        {
          "name": "Test Service Health",
          "request": {
            "method": "GET",
            "url": "{{BASE_URL}}/api/v1/payments/test"
          }
        }
      ]
    }
  ]
}
```

---

## 🎯 TESTING CHECKLIST

- [ ] Service health check
- [ ] Sample data creation
- [ ] Payment request validation
- [ ] Core payment processing
- [ ] Payment retrieval operations
- [ ] Status management
- [ ] Refund processing
- [ ] Job portal specific payments
- [ ] Payment validation
- [ ] Webhook handling
- [ ] Error scenarios
- [ ] Security tests
- [ ] Performance tests

---

## 🚀 NEXT STEPS

1. **Import Collection** → Import the Postman collection
2. **Set Environment** → Configure environment variables
3. **Get JWT Token** → Authenticate and get token
4. **Run Tests** → Execute test scenarios
5. **Verify Results** → Check all responses
6. **Debug Issues** → Fix any failing tests

Your payment service is now ready for comprehensive testing! 🎉

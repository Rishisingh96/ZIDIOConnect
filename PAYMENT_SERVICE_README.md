# ZIDIOConnect Payment Service

A streamlined payment service built with Spring Boot, focused on Razorpay integration for the ZIDIOConnect job portal.

## 🚀 Features

- **Razorpay Integration**: Complete Razorpay payment gateway integration
- **Job Portal Payments**: Specialized payment methods for job postings, premium features, etc.
- **Secure Processing**: JWT-based authentication and role-based access control
- **Webhook Support**: Razorpay webhook handling for payment status updates
- **Refund Management**: Full refund processing capabilities

## 🛠️ Configuration

### Razorpay Credentials

Update your `application.properties`:

```properties
# Razorpay Configuration
razorpay.key.id=rzp_test_R56K4468WrFdky
razorpay.key.secret=x9zIjuFUtTClnewitXVNrC1r
razorpay.currency=INR
```

### Database

The service automatically creates the `payments` table with all necessary fields.

## 📡 API Endpoints

### Test Endpoint

```
GET /api/v1/payments/test
```

Verifies the payment service is working and Razorpay is configured.

### Core Payment Operations

#### Process Payment

```
POST /api/v1/payments/process
```

Process a payment with automatic Razorpay integration.

#### Process Razorpay Payment

```
POST /api/v1/payments/process/razorpay
```

Direct Razorpay payment processing.

### Payment Retrieval

#### Get Payment by ID

```
GET /api/v1/payments/{paymentId}
```

#### Get Payment by Transaction ID

```
GET /api/v1/payments/transaction/{transactionId}
```

#### Get User Payments (Paginated)

```
GET /api/v1/payments/user/{userId}?page=0&size=10&sortBy=createdAt&sortDir=desc
```

### Payment Status Management

#### Verify Payment Status

```
PUT /api/v1/payments/verify/{transactionId}
```

#### Update Payment Status

```
PUT /api/v1/payments/status/{transactionId}?status=SUCCESS
```

#### Razorpay Payment Verification

```
POST /api/v1/payments/razorpay/verify
```

### Refund Operations

#### Process Refund

```
POST /api/v1/payments/refund/{transactionId}?amount=100&reason=Customer request
```

### Job Portal Specific Payments

#### Job Posting Payment

```
POST /api/v1/payments/job-posting?userId=1&jobId=123&jobTitle=Software Engineer&amount=500
```

#### Premium Job Posting Payment

```
POST /api/v1/payments/premium-job-posting?userId=1&jobId=123&jobTitle=Senior Developer&amount=1000
```

#### Featured Profile Payment

```
POST /api/v1/payments/featured-profile?userId=1&profileId=456&amount=300
```

### Payment Validation

#### Check Payment Success

```
GET /api/v1/payments/validate/{transactionId}/successful
```

### Webhook Handling

#### Payment Webhook

```
POST /api/v1/payments/webhook
```

## 💳 Payment Request Format

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

## 🔄 Payment Response Format

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

## 🎯 Payment Purposes

- `JOB_POSTING`: Regular job posting
- `PREMIUM_JOB_POSTING`: Premium/featured job posting
- `FEATURED_PROFILE`: Featured profile promotion
- `RESUME_ACCESS`: Resume access payment
- `INTERVIEW_SCHEDULING`: Interview scheduling payment
- `RECRUITER_VERIFICATION`: Recruiter verification payment
- `PREMIUM_SUBSCRIPTION`: Premium subscription payment
- `ADVERTISING`: Advertising payment
- `CUSTOM_SERVICE`: Custom service payment

## 🔐 Security

- **JWT Authentication**: All endpoints require valid JWT tokens
- **Role-Based Access**: Different endpoints have different role requirements
- **Secure Headers**: CSRF protection and secure headers enabled

### Required Roles

- **USER**: Basic payment operations, profile payments
- **RECRUITER**: Job posting payments, resume access
- **ADMIN**: All operations, refunds, status updates

## 🧪 Testing

### Run Tests

```bash
mvn test -Dtest=PaymentServiceTest
```

### Test Endpoint

```bash
curl -X GET http://localhost:9090/api/v1/payments/test
```

## 🚀 Getting Started

1. **Configure Razorpay**: Update credentials in `application.properties`
2. **Start Application**: Run `mvn spring-boot:run`
3. **Test Service**: Call `/api/v1/payments/test` endpoint
4. **Process Payment**: Use the payment endpoints with proper authentication

## 📊 Database Schema

The `payments` table includes:

- Payment details (amount, currency, status)
- Transaction tracking (transaction ID, gateway ID)
- User and entity relationships
- Timestamps and audit fields
- Payment purpose and method information

## 🔧 Troubleshooting

### Common Issues

1. **Razorpay Connection Error**: Check credentials and network connectivity
2. **Payment Status Not Updated**: Verify webhook configuration
3. **Authentication Error**: Ensure valid JWT token and proper roles

### Logs

Check application logs for detailed error information:

```bash
tail -f logs/application.log
```

## 📚 Dependencies

- Spring Boot 3.5.0
- Spring Security
- Spring Data JPA
- Razorpay Java SDK 1.4.7
- MySQL Database
- JWT Authentication

## 🤝 Support

For issues or questions:

1. Check the logs for error details
2. Verify Razorpay credentials
3. Ensure proper authentication and authorization
4. Test with the `/test` endpoint first

---

**Note**: This service is configured for Razorpay test environment. For production, update credentials and ensure proper security measures.


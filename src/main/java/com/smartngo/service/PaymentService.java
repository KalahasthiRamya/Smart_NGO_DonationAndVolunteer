package com.smartngo.service;

import com.smartngo.dto.PaymentRequest;
import com.smartngo.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
}

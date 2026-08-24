package com.smartngo.dto;

import com.smartngo.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private boolean success;
    private String transactionId;
    private String message;
    private BigDecimal amount;
    private PaymentStatus status;
}

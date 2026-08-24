package com.smartngo.dto;

import com.smartngo.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private Long donorId;
    private Long campaignId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
}

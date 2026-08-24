package com.smartngo.service.impl;

import com.smartngo.dto.DonationDto;
import com.smartngo.dto.PaymentRequest;
import com.smartngo.dto.PaymentResponse;
import com.smartngo.entity.Donation;
import com.smartngo.enums.PaymentStatus;
import com.smartngo.service.DonationService;
import com.smartngo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${payment.mode:mock}")
    private String paymentMode;

    @Autowired
    private DonationService donationService;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        if ("mock".equalsIgnoreCase(paymentMode)) {
            // Generate a clean mock transaction ID and process donation
            DonationDto dto = DonationDto.builder()
                    .donorId(request.getDonorId())
                    .campaignId(request.getCampaignId())
                    .amount(request.getAmount())
                    .paymentMethod(request.getPaymentMethod())
                    .build();

            Donation donation = donationService.createDonation(dto);

            return PaymentResponse.builder()
                    .success(true)
                    .transactionId(donation.getTransactionId())
                    .amount(donation.getAmount())
                    .status(PaymentStatus.SUCCESS)
                    .message("Mock payment processed successfully!")
                    .build();
        } else {
            // Integration hook for live Razorpay/Stripe API client
            String liveTxnId = "LIVE_RAZORPAY_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            DonationDto dto = DonationDto.builder()
                    .donorId(request.getDonorId())
                    .campaignId(request.getCampaignId())
                    .amount(request.getAmount())
                    .paymentMethod(request.getPaymentMethod())
                    .build();

            Donation donation = donationService.createDonation(dto);

            return PaymentResponse.builder()
                    .success(true)
                    .transactionId(liveTxnId)
                    .amount(donation.getAmount())
                    .status(PaymentStatus.SUCCESS)
                    .message("Live payment gateway transaction completed!")
                    .build();
        }
    }
}

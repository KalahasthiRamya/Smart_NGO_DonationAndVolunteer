package com.smartngo.controller;

import com.smartngo.dto.PaymentRequest;
import com.smartngo.dto.PaymentResponse;
import com.smartngo.entity.Campaign;
import com.smartngo.entity.Donor;
import com.smartngo.enums.CampaignStatus;
import com.smartngo.enums.PaymentMethod;

import com.smartngo.security.CustomUserDetails;
import com.smartngo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Collections;

@Controller
@RequestMapping("/donor")
public class DonorController {

    @Autowired
    private DonorService donorService;

    @Autowired
    private DonationService donationService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private NotificationService notificationService;

    private Donor getLoggedInDonor(CustomUserDetails userDetails) {
        return donorService.findByUserId(userDetails.getUser().getId())
                .orElse(null);
    }

    @GetMapping("/dashboard")
    public String donorDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Donor donor = getLoggedInDonor(userDetails);
        if (donor != null) {
            model.addAttribute("donor", donor);
            model.addAttribute("myDonations", donationService.findByDonor(donor));
            model.addAttribute("totalDonated", donor.getTotalDonations());
        }
        model.addAttribute("activeCampaigns", campaignService.findByStatus(CampaignStatus.ACTIVE));
        return "donor/dashboard";
    }

    @GetMapping("/profile")
    public String donorProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("donor", getLoggedInDonor(userDetails));
        return "donor/profile";
    }

    @GetMapping("/campaigns")
    public String donorCampaigns(Model model) {
        model.addAttribute("campaigns", campaignService.findAllCampaigns());
        return "donor/campaigns";
    }

    @GetMapping("/donate")
    public String showDonatePage(@RequestParam(value = "campaignId", required = false) Long campaignId, Model model) {
        model.addAttribute("campaigns", campaignService.findByStatus(CampaignStatus.ACTIVE));
        model.addAttribute("selectedCampaignId", campaignId);
        model.addAttribute("paymentMethods", PaymentMethod.values());
        return "donor/donate";
    }

    @PostMapping("/donate")
    public String processDonation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @RequestParam("campaignId") Long campaignId,
                                  @RequestParam("amount") BigDecimal amount,
                                  @RequestParam("paymentMethod") PaymentMethod paymentMethod,
                                  RedirectAttributes ra) {
        Donor donor = getLoggedInDonor(userDetails);
        if (donor == null) {
            ra.addFlashAttribute("errorMessage", "Donor profile not found.");
            return "redirect:/donor/donate";
        }

        PaymentRequest request = PaymentRequest.builder()
                .donorId(donor.getId())
                .campaignId(campaignId)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .build();

        PaymentResponse response = paymentService.processPayment(request);

        if (response.isSuccess()) {
            ra.addFlashAttribute("successMessage", "Donation Successful! Transaction ID: " + response.getTransactionId());
            return "redirect:/donor/donations";
        } else {
            ra.addFlashAttribute("errorMessage", "Payment failed: " + response.getMessage());
            return "redirect:/donor/donate";
        }
    }

    @GetMapping("/donations")
    public String donationHistory(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Donor donor = getLoggedInDonor(userDetails);
        if (donor != null) {
            model.addAttribute("donations", donationService.findByDonor(donor));
        } else {
            model.addAttribute("donations", Collections.emptyList());
        }
        return "donor/donations";
    }

    @GetMapping("/receipts")
    public String donationReceipts(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Donor donor = getLoggedInDonor(userDetails);
        if (donor != null) {
            model.addAttribute("donations", donationService.findByDonor(donor));
        } else {
            model.addAttribute("donations", Collections.emptyList());
        }
        return "donor/receipts";
    }

    @GetMapping("/impact")
    public String donorImpact(Model model) {
        model.addAttribute("campaigns", campaignService.findAllCampaigns());
        return "donor/impact";
    }
}

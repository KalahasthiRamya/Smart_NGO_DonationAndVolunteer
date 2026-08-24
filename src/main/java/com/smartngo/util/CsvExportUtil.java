package com.smartngo.util;

import com.smartngo.entity.Donation;
import com.smartngo.entity.Volunteer;

import java.math.BigDecimal;
import java.util.List;

public class CsvExportUtil {

    public static byte[] exportDonationsToCsv(List<Donation> donations) {
        StringBuilder sb = new StringBuilder();
        sb.append("Donation ID,Donor Name,Donor Email,Campaign Name,Amount (INR),Payment Method,Transaction ID,Donation Date,Status\n");

        for (Donation d : donations) {
            String donorName = d.getDonor() != null && d.getDonor().getUser() != null ? d.getDonor().getUser().getName() : "N/A";
            String donorEmail = d.getDonor() != null && d.getDonor().getUser() != null ? d.getDonor().getUser().getEmail() : "N/A";
            String campaignName = d.getCampaign() != null ? d.getCampaign().getName() : "N/A";

            sb.append(d.getId()).append(",")
              .append("\"").append(donorName).append("\",")
              .append("\"").append(donorEmail).append("\",")
              .append("\"").append(campaignName).append("\",")
              .append(d.getAmount()).append(",")
              .append(d.getPaymentMethod()).append(",")
              .append(d.getTransactionId()).append(",")
              .append(d.getDonationDate()).append(",")
              .append(d.getStatus()).append("\n");
        }
        return sb.toString().getBytes();
    }

    public static byte[] exportVolunteersToCsv(List<Volunteer> volunteers) {
        StringBuilder sb = new StringBuilder();
        sb.append("Volunteer ID,Name,Email,Phone,Skills,Joined Date,Status\n");

        for (Volunteer v : volunteers) {
            String name = v.getUser() != null ? v.getUser().getName() : "N/A";
            String email = v.getUser() != null ? v.getUser().getEmail() : "N/A";
            String phone = v.getUser() != null ? v.getUser().getPhone() : "N/A";

            sb.append(v.getId()).append(",")
              .append("\"").append(name).append("\",")
              .append("\"").append(email).append("\",")
              .append("\"").append(phone).append("\",")
              .append("\"").append(v.getSkills()).append("\",")
              .append(v.getJoinedDate()).append(",")
              .append(v.getStatus()).append("\n");
        }
        return sb.toString().getBytes();
    }

    public static byte[] exportImpactReportToCsv(BigDecimal totalDonations, long totalDonors, long totalVolunteers, long completedTasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("Metric,Value\n");
        sb.append("Total Funds Collected (INR),").append(totalDonations).append("\n");
        sb.append("Total Donors,").append(totalDonors).append("\n");
        sb.append("Total Active Volunteers,").append(totalVolunteers).append("\n");
        sb.append("Completed Activities/Tasks,").append(completedTasks).append("\n");
        return sb.toString().getBytes();
    }
}

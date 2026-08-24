package com.smartngo.service;

import java.util.Map;

public interface ReportService {
    Map<String, Object> generateDonationReport();
    Map<String, Object> generateVolunteerReport();
    Map<String, Object> generateImpactReport();
    byte[] exportDonationsCsv();
    byte[] exportVolunteersCsv();
    byte[] exportImpactCsv();
}

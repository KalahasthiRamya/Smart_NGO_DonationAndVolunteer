package com.smartngo.repository;

import com.smartngo.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByReportTypeOrderByGeneratedAtDesc(String reportType);
}

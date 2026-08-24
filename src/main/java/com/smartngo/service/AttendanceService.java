package com.smartngo.service;

import com.smartngo.entity.Attendance;
import com.smartngo.entity.Volunteer;
import com.smartngo.enums.AttendanceStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    Attendance recordAttendance(Long volunteerId, Long taskId, LocalDate date, AttendanceStatus status);
    List<Attendance> findAllAttendance();
    List<Attendance> findByVolunteer(Volunteer volunteer);
    int calculateAttendancePercentage(Volunteer volunteer);
}

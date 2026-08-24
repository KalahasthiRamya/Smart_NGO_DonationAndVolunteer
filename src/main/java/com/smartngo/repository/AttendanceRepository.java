package com.smartngo.repository;

import com.smartngo.entity.Attendance;
import com.smartngo.entity.Volunteer;
import com.smartngo.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByVolunteer(Volunteer volunteer);
    long countByVolunteerAndStatus(Volunteer volunteer, AttendanceStatus status);
    long countByVolunteer(Volunteer volunteer);
}

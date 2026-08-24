package com.smartngo.service.impl;

import com.smartngo.entity.Attendance;
import com.smartngo.entity.Task;
import com.smartngo.entity.Volunteer;
import com.smartngo.enums.AttendanceStatus;
import com.smartngo.exception.ResourceNotFoundException;
import com.smartngo.repository.AttendanceRepository;
import com.smartngo.repository.TaskRepository;
import com.smartngo.repository.VolunteerRepository;
import com.smartngo.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Attendance recordAttendance(Long volunteerId, Long taskId, LocalDate date, AttendanceStatus status) {
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + volunteerId));

        Task task = null;
        if (taskId != null) {
            task = taskRepository.findById(taskId).orElse(null);
        }

        Attendance attendance = Attendance.builder()
                .volunteer(volunteer)
                .task(task)
                .attendanceDate(date != null ? date : LocalDate.now())
                .status(status != null ? status : AttendanceStatus.PRESENT)
                .build();

        return attendanceRepository.save(attendance);
    }

    @Override
    public List<Attendance> findAllAttendance() {
        return attendanceRepository.findAll();
    }

    @Override
    public List<Attendance> findByVolunteer(Volunteer volunteer) {
        return attendanceRepository.findByVolunteer(volunteer);
    }

    @Override
    public int calculateAttendancePercentage(Volunteer volunteer) {
        long total = attendanceRepository.countByVolunteer(volunteer);
        if (total == 0) return 100;
        long present = attendanceRepository.countByVolunteerAndStatus(volunteer, AttendanceStatus.PRESENT);
        return (int) Math.round((present * 100.0) / total);
    }
}

package com.smartngo.service;

import com.smartngo.entity.User;
import com.smartngo.entity.Volunteer;

import java.util.List;
import java.util.Optional;

public interface VolunteerService {
    List<Volunteer> findAllVolunteers();
    Optional<Volunteer> findById(Long id);
    Optional<Volunteer> findByUserId(Long userId);
    Volunteer saveVolunteer(Volunteer volunteer);
    Volunteer registerVolunteer(User user, String skills);
    Volunteer updateVolunteerSkillsAndStatus(Long id, String skills, String status);
    void deleteVolunteer(Long id);
    List<Volunteer> searchVolunteers(String keyword);
    long countActiveVolunteers();
}

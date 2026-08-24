package com.smartngo.service.impl;

import com.smartngo.entity.Volunteer;
import com.smartngo.exception.ResourceNotFoundException;
import com.smartngo.repository.VolunteerRepository;
import com.smartngo.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Override
    public List<Volunteer> findAllVolunteers() {
        return volunteerRepository.findAll();
    }

    @Override
    public Optional<Volunteer> findById(Long id) {
        return volunteerRepository.findById(id);
    }

    @Override
    public Optional<Volunteer> findByUserId(Long userId) {
        return volunteerRepository.findByUserId(userId);
    }

    @Override
    public Volunteer saveVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    @Override
    public Volunteer updateVolunteerSkillsAndStatus(Long id, String skills, String status) {
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + id));
        if (skills != null) {
            volunteer.setSkills(skills);
        }
        if (status != null) {
            volunteer.setStatus(status);
            if (volunteer.getUser() != null) {
                volunteer.getUser().setStatus(status);
            }
        }
        return volunteerRepository.save(volunteer);
    }

    @Override
    public void deleteVolunteer(Long id) {
        if (!volunteerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Volunteer not found with id: " + id);
        }
        volunteerRepository.deleteById(id);
    }

    @Override
    public List<Volunteer> searchVolunteers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return volunteerRepository.findAll();
        }
        return volunteerRepository.searchVolunteers(keyword.trim());
    }

    @Override
    public long countActiveVolunteers() {
        return volunteerRepository.countByStatus("ACTIVE");
    }
}

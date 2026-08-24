package com.smartngo.repository;

import com.smartngo.entity.User;
import com.smartngo.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Optional<Volunteer> findByUser(User user);
    Optional<Volunteer> findByUserId(Long userId);
    List<Volunteer> findByStatus(String status);
    long countByStatus(String status);

    @Query("SELECT v FROM Volunteer v WHERE LOWER(v.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(v.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(v.skills) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Volunteer> searchVolunteers(@Param("keyword") String keyword);
}

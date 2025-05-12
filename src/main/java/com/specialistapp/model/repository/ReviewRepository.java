package com.specialistapp.model.repository;

import com.specialistapp.model.entity.Review;
import com.specialistapp.model.entity.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findBySpecialist(Specialist specialist);
}
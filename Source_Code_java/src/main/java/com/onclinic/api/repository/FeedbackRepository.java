package com.onclinic.api.repository;
import com.onclinic.api.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FeedbackRepository extends JpaRepository<Feedback, Long> { List<Feedback> findByClinicIdOrderByCreatedAtDesc(Long clinicId); }

package com.onclinic.api.repository;
import com.onclinic.api.model.ExaminationSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SlotRepository extends JpaRepository<ExaminationSlot, Long> { List<ExaminationSlot> findByClinicIdOrderByStartTimeAsc(Long clinicId); }

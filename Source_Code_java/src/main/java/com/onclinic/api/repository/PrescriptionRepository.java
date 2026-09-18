package com.onclinic.api.repository;
import com.onclinic.api.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> { List<Prescription> findByPatientIdOrderByCreatedAtDesc(Long patientId); List<Prescription> findByDoctorIdOrderByCreatedAtDesc(Long doctorId); }

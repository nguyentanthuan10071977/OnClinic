package com.onclinic.api.repository;
import com.onclinic.api.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ClinicRepository extends JpaRepository<Clinic, Long> { Optional<Clinic> findByDoctorId(Long doctorId); }

package com.onclinic.api.repository;
import com.onclinic.api.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AppointmentRepository extends JpaRepository<Appointment, Long> { List<Appointment> findByPatientIdOrderBySlotStartTimeAsc(Long patientId); List<Appointment> findByClinicDoctorIdOrderBySlotStartTimeAsc(Long doctorId); }

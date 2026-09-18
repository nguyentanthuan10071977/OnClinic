package com.onclinic.api.repository;
import com.onclinic.api.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface PaymentRepository extends JpaRepository<Payment, Long> { Optional<Payment> findByAppointmentId(Long appointmentId); }

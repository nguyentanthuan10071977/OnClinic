package com.onclinic.api.controller;

import com.onclinic.api.model.*;
import com.onclinic.api.repository.*;
import com.onclinic.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController @RequestMapping("/api/payments") @RequiredArgsConstructor
public class PaymentController {
    private final PaymentRepository payments;
    private final AppointmentRepository appointments;
    private final AuthService auth;

    @PostMapping("/patient")
    public Payment pay(@RequestBody Map<String,Object> body, java.security.Principal p) {
        User patient = auth.current(p.getName());
        Long appointmentId = ((Number)body.get("appointmentId")).longValue();
        Appointment a = appointments.findById(appointmentId).orElseThrow();
        if (!a.getPatient().getId().equals(patient.getId())) throw new IllegalStateException("Not your appointment");
        double amount = ((Number)body.getOrDefault("amount",0)).doubleValue();
        if (amount<=0) throw new IllegalArgumentException("Amount must be positive");
        Payment pay = Payment.builder().appointment(a).amount(amount).status(Payment.Status.PAID)
                .transactionRef("DEMO-"+UUID.randomUUID()).createdAt(LocalDateTime.now()).build();
        return payments.save(pay);
    }

    @GetMapping("/doctor")
    public List<Payment> doctorPayments(java.security.Principal p) {
        User doctor = auth.current(p.getName());
        return payments.findAll().stream()
                .filter(x -> x.getAppointment().getClinic().getDoctor().getId().equals(doctor.getId())).toList();
    }
}
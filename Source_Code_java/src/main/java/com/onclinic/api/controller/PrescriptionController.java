package com.onclinic.api.controller;

import com.onclinic.api.model.*;
import com.onclinic.api.repository.*;
import com.onclinic.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController @RequestMapping("/api/prescriptions") @RequiredArgsConstructor
public class PrescriptionController {
    private final PrescriptionRepository prescriptions;
    private final UserRepository users;
    private final AuthService auth;

    @PostMapping
    public Prescription create(@RequestBody Prescription input, java.security.Principal p) {
        User doctor = auth.current(p.getName());
        if (doctor.getRole()!=User.Role.DOCTOR) throw new IllegalStateException("Doctor role required");
        User patient = users.findById(input.getPatient().getId()).orElseThrow();
        if (input.getItems()==null || input.getItems().isEmpty()) throw new IllegalArgumentException("Prescription items are required");
        input.getItems().forEach(i -> {
            if (i.getMedicineName()==null || i.getMedicineName().isBlank() || i.getDosage()==null || i.getDosage()<0
                    || i.getUnit()==null || i.getUnit().isBlank())
                throw new IllegalArgumentException("Invalid medicine item");
        });
        input.setId(null); input.setDoctor(doctor); input.setPatient(patient); input.setCreatedAt(LocalDateTime.now());
        return prescriptions.save(input);
    }

    @GetMapping("/mine")
    public List<Prescription> mine(java.security.Principal p) {
        User u = auth.current(p.getName());
        return u.getRole()==User.Role.DOCTOR
                ? prescriptions.findByDoctorIdOrderByCreatedAtDesc(u.getId())
                : prescriptions.findByPatientIdOrderByCreatedAtDesc(u.getId());
    }
}
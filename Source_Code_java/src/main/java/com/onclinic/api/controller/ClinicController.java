package com.onclinic.api.controller;

import com.onclinic.api.model.*;
import com.onclinic.api.repository.*;
import com.onclinic.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/clinics") @RequiredArgsConstructor
public class ClinicController {
    private final ClinicRepository clinics;
    private final UserRepository users;
    private final AuthService auth;

    @PostMapping
    public Clinic create(@RequestBody Clinic input, java.security.Principal p) {
        User doctor = auth.current(p.getName());
        if (doctor.getRole() != User.Role.DOCTOR) throw new IllegalStateException("Doctor role required");
        if (input.getName()==null || input.getName().isBlank() || input.getAddress()==null || input.getAddress().isBlank()
                || input.getPhone()==null || input.getPhone().isBlank()) throw new IllegalArgumentException("Required clinic information is missing");
        input.setId(null); input.setDoctor(doctor);
        return clinics.save(input);
    }

    @GetMapping("/{id}")
    public Clinic get(@PathVariable Long id) { return clinics.findById(id).orElseThrow(); }

    @GetMapping("/mine")
    public Clinic mine(java.security.Principal p) {
        return clinics.findByDoctorId(auth.current(p.getName()).getId()).orElseThrow();
    }

    @PutMapping("/mine")
    public Clinic update(@RequestBody Clinic input, java.security.Principal p) {
        Clinic c = mine(p);
        if (input.getName()==null || input.getName().isBlank() || input.getAddress()==null || input.getAddress().isBlank()
                || input.getPhone()==null || input.getPhone().isBlank()) throw new IllegalArgumentException("Required clinic information is missing");
        c.setName(input.getName()); c.setAddress(input.getAddress()); c.setPhone(input.getPhone());
        c.setDescription(input.getDescription()); c.setOnlineExaminationSupported(input.isOnlineExaminationSupported());
        return clinics.save(c);
    }

    @GetMapping
    public List<Clinic> all() { return clinics.findAll(); }
}
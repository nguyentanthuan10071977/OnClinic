package com.onclinic.api.controller;

import com.onclinic.api.model.*;
import com.onclinic.api.repository.*;
import com.onclinic.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController @RequestMapping("/api/slots") @RequiredArgsConstructor
public class SlotController {
    private final SlotRepository slots;
    private final ClinicRepository clinics;
    private final AuthService auth;

    @PostMapping
    public ExaminationSlot create(@RequestBody ExaminationSlot input, java.security.Principal p) {
        User doctor = auth.current(p.getName());
        Clinic clinic = clinics.findById(input.getClinic().getId()).orElseThrow();
        if (!clinic.getDoctor().getId().equals(doctor.getId())) throw new IllegalStateException("Not your clinic");
        if (input.getStartTime()==null || input.getEndTime()==null || !input.getStartTime().isAfter(LocalDateTime.now())
                || !input.getEndTime().isAfter(input.getStartTime())) throw new IllegalArgumentException("Invalid future time range");
        input.setId(null); input.setClinic(clinic); input.setBooked(false);
        return slots.save(input);
    }

    @GetMapping("/clinic/{clinicId}")
    public List<ExaminationSlot> list(@PathVariable Long clinicId) { return slots.findByClinicIdOrderByStartTimeAsc(clinicId); }

    @PutMapping("/{id}")
    public ExaminationSlot update(@PathVariable Long id, @RequestBody ExaminationSlot input, java.security.Principal p) {
        ExaminationSlot s = slots.findById(id).orElseThrow();
        User doctor = auth.current(p.getName());
        if (!s.getClinic().getDoctor().getId().equals(doctor.getId())) throw new IllegalStateException("Not your slot");
        if (input.getStartTime()==null || input.getEndTime()==null || !input.getStartTime().isAfter(LocalDateTime.now())
                || !input.getEndTime().isAfter(input.getStartTime())) throw new IllegalArgumentException("Invalid future time range");
        s.setStartTime(input.getStartTime()); s.setEndTime(input.getEndTime());
        return slots.save(s);
    }
}
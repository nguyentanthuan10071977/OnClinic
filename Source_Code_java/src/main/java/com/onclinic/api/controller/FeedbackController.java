package com.onclinic.api.controller;

import com.onclinic.api.model.*;
import com.onclinic.api.repository.*;
import com.onclinic.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController @RequestMapping("/api/feedback") @RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackRepository feedback;
    private final ClinicRepository clinics;
    private final AuthService auth;

    @GetMapping("/clinic/{clinicId}")
    public List<Feedback> list(@PathVariable Long clinicId) { return feedback.findByClinicIdOrderByCreatedAtDesc(clinicId); }

    @PostMapping("/clinic/{clinicId}")
    public Feedback create(@PathVariable Long clinicId, @RequestBody Feedback input, java.security.Principal p) {
        User patient = auth.current(p.getName());
        if (patient.getRole()!=User.Role.PATIENT) throw new IllegalStateException("Patient role required");
        if (input.getRating()<0 || input.getRating()>5 || Math.round(input.getRating()*2)!=input.getRating()*2)
            throw new IllegalArgumentException("Rating must be between 0 and 5 in 0.5 steps");
        Feedback f = Feedback.builder().clinic(clinics.findById(clinicId).orElseThrow()).patient(patient)
                .rating(input.getRating()).comment(input.getComment()).createdAt(LocalDateTime.now()).build();
        return feedback.save(f);
    }
}
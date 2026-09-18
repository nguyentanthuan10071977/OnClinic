package com.onclinic.api.controller;

import com.onclinic.api.model.Clinic;
import com.onclinic.api.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/recommendations") @RequiredArgsConstructor
public class RecommendationController {
    private final ClinicRepository clinics;

    @PostMapping("/clinic")
    public Map<String,Object> recommend(@RequestBody Map<String,Object> body) {
        Object symptoms = body.get("symptoms");
        if (!(symptoms instanceof List<?> list) || list.size()!=3)
            throw new IllegalArgumentException("Enter exactly three obvious symptoms");
        // Demo only: the user story requests disease prediction and clinic suggestions.
        // This deliberately returns a transparent placeholder instead of claiming medical accuracy.
        List<Clinic> result = clinics.findAll();
        return Map.of("symptoms", list,
                "predictedDisease", "DEMO_RESULT_REQUIRES_MEDICAL_MODEL",
                "clinics", result,
                "disclaimer", "This endpoint is a demo integration point; it is not a medical diagnosis.");
    }
}
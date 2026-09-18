package com.onclinic.api.controller;

import com.onclinic.api.model.*;
import com.onclinic.api.repository.*;
import com.onclinic.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/appointments") @RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentRepository appointments;
    private final SlotRepository slots;
    private final ClinicRepository clinics;
    private final AuthService auth;

    @PostMapping
    public Appointment book(@RequestBody Map<String,Object> body, java.security.Principal p) {
        User patient = auth.current(p.getName());
        if (patient.getRole()!=User.Role.PATIENT) throw new IllegalStateException("Patient role required");
        Long slotId = ((Number)body.get("slotId")).longValue();
        ExaminationSlot slot = slots.findById(slotId).orElseThrow();
        if (slot.isBooked()) throw new IllegalStateException("Slot already booked");
        String type = String.valueOf(body.getOrDefault("type","OFFLINE"));
        if ("ONLINE".equals(type) && !slot.getClinic().isOnlineExaminationSupported())
            throw new IllegalArgumentException("Clinic does not support online examination");
        slot.setBooked(true); slots.save(slot);
        Appointment a = Appointment.builder().patient(patient).clinic(slot.getClinic()).slot(slot)
                .type(Appointment.ExaminationType.valueOf(type))
                .status(Appointment.Status.BOOKED)
                .videoRoomId("ONLINE".equals(type) ? UUID.randomUUID().toString() : null).build();
        return appointments.save(a);
    }

    @GetMapping("/upcoming/patient")
    public List<Appointment> patientUpcoming(java.security.Principal p) {
        return appointments.findByPatientIdOrderBySlotStartTimeAsc(auth.current(p.getName()).getId()).stream()
                .filter(a -> a.getStatus()==Appointment.Status.BOOKED).toList();
    }

    @GetMapping("/upcoming/doctor")
    public List<Appointment> doctorUpcoming(java.security.Principal p) {
        return appointments.findByClinicDoctorIdOrderBySlotStartTimeAsc(auth.current(p.getName()).getId()).stream()
                .filter(a -> a.getStatus()==Appointment.Status.BOOKED).toList();
    }

    @GetMapping("/history/patient")
    public List<Appointment> patientHistory(java.security.Principal p) {
        return appointments.findByPatientIdOrderBySlotStartTimeAsc(auth.current(p.getName()).getId()).stream()
                .filter(a -> a.getStatus()==Appointment.Status.COMPLETED).toList();
    }

    @GetMapping("/history/doctor")
    public List<Appointment> doctorHistory(java.security.Principal p) {
        return appointments.findByClinicDoctorIdOrderBySlotStartTimeAsc(auth.current(p.getName()).getId()).stream()
                .filter(a -> a.getStatus()==Appointment.Status.COMPLETED).toList();
    }

    @PostMapping("/{id}/complete")
    public Appointment complete(@PathVariable Long id) {
        Appointment a = appointments.findById(id).orElseThrow();
        a.setStatus(Appointment.Status.COMPLETED);
        return appointments.save(a);
    }

    @GetMapping("/{id}/online-session")
    public Map<String,String> onlineSession(@PathVariable Long id) {
        Appointment a = appointments.findById(id).orElseThrow();
        if (a.getType()!=Appointment.ExaminationType.ONLINE) throw new IllegalStateException("Not an online appointment");
        return Map.of("appointmentId", id.toString(), "roomId", a.getVideoRoomId(),
                "note", "Connect this roomId to WebRTC/Twilio/Agora in production.");
    }
}
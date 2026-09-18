package com.onclinic.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Appointment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    private User patient;

    @ManyToOne(optional=false)
    private Clinic clinic;

    @OneToOne(optional=false)
    private ExaminationSlot slot;

    @Enumerated(EnumType.STRING)
    private ExaminationType type;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String videoRoomId;

    public enum ExaminationType { OFFLINE, ONLINE }
    public enum Status { BOOKED, COMPLETED, CANCELLED }
}
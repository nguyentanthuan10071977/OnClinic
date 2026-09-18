package com.onclinic.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    private Appointment appointment;

    private double amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String transactionRef;
    private LocalDateTime createdAt;

    public enum Status { PENDING, PAID, FAILED }
}
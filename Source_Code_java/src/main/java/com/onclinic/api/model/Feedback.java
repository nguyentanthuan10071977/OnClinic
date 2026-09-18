package com.onclinic.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"clinic_id","patient_id"}))
public class Feedback {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    private Clinic clinic;

    @ManyToOne(optional=false)
    private User patient;

    @Column(nullable=false)
    private double rating;

    @Column(length=2000)
    private String comment;

    private LocalDateTime createdAt;
}
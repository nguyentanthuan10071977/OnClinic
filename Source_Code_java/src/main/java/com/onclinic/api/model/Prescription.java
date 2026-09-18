package com.onclinic.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Prescription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    private User patient;

    @ManyToOne(optional=false)
    private User doctor;

    private LocalDateTime createdAt;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    @Builder.Default
    private List<PrescriptionItem> items = new ArrayList<>();
}

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class PrescriptionItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicineName;
    private Double dosage;
    private String unit;
    private String instruction;
}
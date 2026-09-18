package com.onclinic.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Clinic {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String address;

    @Column(nullable=false)
    private String phone;

    private String description;
    private boolean onlineExaminationSupported;

    @OneToOne(optional=false)
    private User doctor;
}
package org.example.hospitalappointmentschedulingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Fetch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Future
    private LocalDateTime dateTime;

    @NotBlank
    private String doctor;

    @NotBlank
    private String service;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "appointment",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Visit> visits = new ArrayList<>();
}

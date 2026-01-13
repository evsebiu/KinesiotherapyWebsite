package com.example.KinetoWebsite.Model.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is required.")
    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @NotNull(message = "Phone number is required for appointment.")
    @Column(name = "phone_number", nullable = false)
    String phoneNumber;

    @NotNull(message = "Date is required.")
    @Column(name = "local_date", nullable = false)
    private LocalDate date;

    @Email
    private String customerEmail;


    @Column(name = "additional_info")
    private String additionalInfo;

    private String serviceName;
    private String time;
    private String status = "NOU";




}

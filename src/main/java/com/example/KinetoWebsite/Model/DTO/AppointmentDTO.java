package com.example.KinetoWebsite.Model.DTO;


import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {

    private Long id;

    private String chaptchaResponse;

    @NotNull(message = "Name is required.")
    @Size(min = 2, max = 25, message = "Name should be between 2 and 25 characters.")
    private String patientName;

    @NotNull(message = "Phone number is required for appointment.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    String phoneNumber;

    @NotNull(message = "Date is required.")
    @FutureOrPresent(message = "Appointment date must be today or in the future")
    private LocalDate date;


    @Email
    String customerEmail;

    private String additionalInfo;

    private String serviceName;
    private String time;
    private String status = "NOU";
    

}

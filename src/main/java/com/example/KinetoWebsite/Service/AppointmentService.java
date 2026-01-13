package com.example.KinetoWebsite.Service;

import com.example.KinetoWebsite.Model.DTO.AppointmentDTO;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {


    //GET
    List<AppointmentDTO> getAllAppointments();
    Optional<AppointmentDTO> getAppointmentById(Long id);


    //business logic
    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);
    void deleteAppointment(Long id);
    AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO);
}

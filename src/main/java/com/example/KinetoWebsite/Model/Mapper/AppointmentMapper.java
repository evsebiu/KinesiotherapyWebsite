package com.example.KinetoWebsite.Model.Mapper;

import com.example.KinetoWebsite.Model.DTO.AppointmentDTO;
import com.example.KinetoWebsite.Model.Entity.Appointment;
import org.springframework.stereotype.Component;


@Component
public class AppointmentMapper {

    public AppointmentDTO toDTO(Appointment appointment) {
        if (appointment == null) return null;

        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setPatientName(appointment.getPatientName());
        dto.setPhoneNumber(appointment.getPhoneNumber());
        dto.setDate(appointment.getDate());
        dto.setAdditionalInfo(appointment.getAdditionalInfo());
        dto.setServiceName(appointment.getServiceName());
        dto.setTime(appointment.getTime());
        dto.setStatus(appointment.getStatus());
        dto.setCustomerEmail(appointment.getCustomerEmail());

        return dto;
    }

    public Appointment toEntity(AppointmentDTO dto) {
        if (dto == null) return null;

        Appointment appointment = new Appointment();
        appointment.setId(dto.getId());
        appointment.setPatientName(dto.getPatientName());
        appointment.setPhoneNumber(dto.getPhoneNumber());
        appointment.setDate(dto.getDate());
        appointment.setAdditionalInfo(dto.getAdditionalInfo());
        appointment.setServiceName(dto.getServiceName());
        appointment.setTime(dto.getTime());
        appointment.setStatus(dto.getStatus() != null ? dto.getStatus() : "NOU");
        appointment.setCustomerEmail(dto.getCustomerEmail());
        return appointment;
    }
}

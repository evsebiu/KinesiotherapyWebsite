package com.example.KinetoWebsite.Service;

import com.example.KinetoWebsite.Model.DTO.AppointmentDTO;
import com.example.KinetoWebsite.Model.Entity.Appointment;
import com.example.KinetoWebsite.Model.Mapper.AppointmentMapper;
import com.example.KinetoWebsite.Repository.AppointmentRepository;
import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service

public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final EmailService emailService;

    AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                           AppointmentMapper appointmentMapper,
                           EmailService emailService){
        this.appointmentMapper = appointmentMapper;
        this.appointmentRepository=appointmentRepository;
        this.emailService=emailService;
    }

    @Override
    public List<AppointmentDTO> getAllAppointments(){
        return appointmentRepository.findAll()
                .stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AppointmentDTO> getAppointmentById(Long id){
        return appointmentRepository.findById(id)
                .map(appointmentMapper::toDTO);
    }

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO){
        if (appointmentDTO == null){
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        Appointment appointmentDetails = appointmentMapper.toEntity(appointmentDTO);
        Appointment savedAppointment = appointmentRepository.save(appointmentDetails);

        return appointmentMapper.toDTO(savedAppointment);
    }

    @Override
    public void deleteAppointment(Long id){
        appointmentRepository.deleteById(id);
    }

    @Override
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO){

        Appointment existingAppointment = appointmentRepository.findById(id)
                        .orElseThrow(()-> new com.example.KinetoWebsite.Exceptions.IllegalArgumentException("" +
                                "Appointment with id: " + id + " not found"));

        existingAppointment.setPatientName(appointmentDTO.getPatientName());
        existingAppointment.setPhoneNumber(appointmentDTO.getPhoneNumber());
        existingAppointment.setDate(appointmentDTO.getDate());
        existingAppointment.setAdditionalInfo(appointmentDTO.getAdditionalInfo());
        existingAppointment.setServiceName(appointmentDTO.getServiceName());
        existingAppointment.setTime(appointmentDTO.getTime());
        existingAppointment.setStatus(appointmentDTO.getStatus());

        Appointment updatedAppointment  = appointmentRepository.save(existingAppointment);
        return appointmentMapper.toDTO(updatedAppointment);
    }
}

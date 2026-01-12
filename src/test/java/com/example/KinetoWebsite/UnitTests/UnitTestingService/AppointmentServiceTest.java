package com.example.KinetoWebsite.UnitTests.UnitTestingService;


import com.example.KinetoWebsite.Model.DTO.AppointmentDTO;
import com.example.KinetoWebsite.Model.Entity.Appointment;
import com.example.KinetoWebsite.Model.Mapper.AppointmentMapper;
import com.example.KinetoWebsite.Repository.AppointmentRepository;
import com.example.KinetoWebsite.Service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

   @Mock
    private AppointmentRepository appointmentRepository;

   @Mock
    private AppointmentMapper appointmentMapper;

   @InjectMocks
    private AppointmentService appointmentService;

   private AppointmentDTO sampleDTO;
   private Appointment sampleEntity;

   @BeforeEach
    void setUp(){
       sampleDTO = new AppointmentDTO();
       sampleDTO.setChaptchaResponse("positive");
       sampleDTO.setDate(LocalDate.ofEpochDay(21-12-2025));
       sampleDTO.setPhoneNumber("+4999999999");
       sampleDTO.setPatientName("Matei Ion");
       sampleDTO.setAdditionalInfo("No extra info.");

       sampleEntity=new Appointment();
       sampleEntity.setId(10L);
       sampleEntity.setServiceName("Masaj cu uleiuri");
       sampleEntity.setPhoneNumber("4075541873");
   }

   @Test
    void getAllAppointments_ShouldReturnTrue(){
       // arrange

       when(appointmentRepository.findAll()).thenReturn(List.of(sampleEntity));
       when(appointmentMapper.toDTO(sampleEntity)).thenReturn(sampleDTO);

       //act
       List<AppointmentDTO> result = appointmentService.getAllAppointments();

       //assert
       assertFalse(result.isEmpty());
       assertEquals(1, result.size());
       assertEquals(sampleDTO.getPatientName(), result.get(0).getPatientName());
       verify(appointmentRepository).findAll();
   }

}

package com.example.KinetoWebsite.UnitTests.UnitTestingService;


import com.example.KinetoWebsite.Model.DTO.AppointmentDTO;
import com.example.KinetoWebsite.Model.Entity.Appointment;
import com.example.KinetoWebsite.Model.Mapper.AppointmentMapper;
import com.example.KinetoWebsite.Repository.AppointmentRepository;
import com.example.KinetoWebsite.Service.AppointmentService;
import com.example.KinetoWebsite.Service.AppointmentServiceImpl;
import com.example.KinetoWebsite.Service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

   @Mock
    private AppointmentRepository appointmentRepository;

   @Mock
    private AppointmentMapper appointmentMapper;

   @Mock
   private EmailService emailService;

   @InjectMocks
    private AppointmentServiceImpl appointmentService;

   private AppointmentDTO sampleDTO;
   private Appointment sampleEntity;

   @BeforeEach
    void setUp(){
       sampleDTO = new AppointmentDTO();
       sampleDTO.setChaptchaResponse("positive");
       sampleDTO.setDate(LocalDate.ofEpochDay(03-03-2026));
       sampleDTO.setPhoneNumber("+4999999999");
       sampleDTO.setPatientName("Matei Ion");
       sampleDTO.setAdditionalInfo("No extra info.");

       sampleEntity=new Appointment();
       sampleEntity.setId(10L);
       sampleEntity.setServiceName("Masaj cu uleiuri");
       sampleEntity.setPhoneNumber("4075541873");
   }
// --- TESTE PENTRU getAllAppointments ---

    @Test
    void getAllAppointments_ShouldReturnList() {
        // arrange
        when(appointmentRepository.findAll()).thenReturn(List.of(sampleEntity));
        when(appointmentMapper.toDTO(sampleEntity)).thenReturn(sampleDTO);

        // act
        List<AppointmentDTO> result = appointmentService.getAllAppointments();

        // assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(sampleDTO.getPatientName(), result.get(0).getPatientName());
        verify(appointmentRepository).findAll();
    }

    @Test
    void getAllAppointments_WhenNoAppointment_ShouldReturnEmptyList() {
        // arrange
        when(appointmentRepository.findAll()).thenReturn(Collections.emptyList());

        // act
        List<AppointmentDTO> result = appointmentService.getAllAppointments();

        // assert
        assertTrue(result.isEmpty());
        verify(appointmentMapper, never()).toDTO(any());
    }

    // --- TESTE PENTRU getAppointmentById ---

    @Test
    void getAppointmentById_WhenExists_ShouldReturnDTO() {
        // arrange
        Long id = 10L;
        when(appointmentRepository.findById(id)).thenReturn(Optional.of(sampleEntity));
        when(appointmentMapper.toDTO(sampleEntity)).thenReturn(sampleDTO);

        // act
        Optional<AppointmentDTO> result = appointmentService.getAppointmentById(id);

        // assert
        assertTrue(result.isPresent());
        assertEquals(sampleDTO.getPatientName(), result.get().getPatientName());
        verify(appointmentRepository).findById(id);
    }

    @Test
    void getAppointmentById_WhenNotExists_ShouldReturnEmpty() {
        // arrange
        Long id = 99L;
        when(appointmentRepository.findById(id)).thenReturn(Optional.empty());

        // act
        Optional<AppointmentDTO> result = appointmentService.getAppointmentById(id);

        // assert
        assertTrue(result.isEmpty());
        verify(appointmentMapper, never()).toDTO(any());
    }

    // --- TESTE PENTRU createAppointment ---

    @Test
    void createAppointment_WhenValid_ShouldReturnSavedDTO() {
        // arrange
        when(appointmentMapper.toEntity(sampleDTO)).thenReturn(sampleEntity);
        when(appointmentRepository.save(sampleEntity)).thenReturn(sampleEntity);
        when(appointmentMapper.toDTO(sampleEntity)).thenReturn(sampleDTO);

        // act
        AppointmentDTO result = appointmentService.createAppointment(sampleDTO);

        // assert
        assertNotNull(result);
        assertEquals("Matei Ion", result.getPatientName());
        verify(appointmentRepository).save(sampleEntity);
    }

    @Test
    void createAppointment_WhenNull_ShouldThrowException() {
        // act & assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.createAppointment(null);
        });

        assertEquals("Appointment cannot be null", exception.getMessage());
        verify(appointmentRepository, never()).save(any());
    }

    // --- TESTE PENTRU updateAppointment ---

    @Test
    void updateAppointment_WhenExists_ShouldUpdateAndReturnDTO() {
        // arrange
        Long id = 10L;
        AppointmentDTO updateData = new AppointmentDTO();
        updateData.setPatientName("Nume Actualizat");
        updateData.setPhoneNumber("0700000000");
        updateData.setServiceName("Fizioterapie");

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(sampleEntity));
        // Simulăm salvarea entității actualizate
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mockuim mapperul pentru a returna un DTO cu datele noi
        AppointmentDTO updatedDTO = new AppointmentDTO();
        updatedDTO.setPatientName("Nume Actualizat");
        when(appointmentMapper.toDTO(any(Appointment.class))).thenReturn(updatedDTO);

        // act
        AppointmentDTO result = appointmentService.updateAppointment(id, updateData);

        // assert
        assertNotNull(result);
        assertEquals("Nume Actualizat", result.getPatientName()); // Verificăm ce returnează mapperul

        // Verificăm dacă entitatea a fost modificată corect înainte de save
        assertEquals("Nume Actualizat", sampleEntity.getPatientName());
        assertEquals("Fizioterapie", sampleEntity.getServiceName());

        verify(appointmentRepository).save(sampleEntity);
    }

    @Test
    void updateAppointment_WhenNotExists_ShouldThrowCustomException() {
        // arrange
        Long id = 99L;
        when(appointmentRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        // Atenție: Aici prindem excepția custom din pachetul Exceptions, nu java.lang
        assertThrows(com.example.KinetoWebsite.Exceptions.IllegalArgumentException.class, () -> {
            appointmentService.updateAppointment(id, sampleDTO);
        });

        verify(appointmentRepository, never()).save(any());
    }

    // --- TESTE PENTRU deleteAppointment ---

    @Test
    void deleteAppointment_ShouldCallRepoDelete() {
        // arrange
        Long id = 10L;
        doNothing().when(appointmentRepository).deleteById(id);

        // act
        appointmentService.deleteAppointment(id);

        // assert
        verify(appointmentRepository, times(1)).deleteById(id);
    }
}

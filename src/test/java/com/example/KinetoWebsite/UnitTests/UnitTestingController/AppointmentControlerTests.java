package com.example.KinetoWebsite.UnitTests.UnitTestingController;

import com.example.KinetoWebsite.Controller.AppointmentController;
import com.example.KinetoWebsite.Model.DTO.AppointmentDTO;
import com.example.KinetoWebsite.Service.AppointmentService;
import com.example.KinetoWebsite.Service.EmailService;
import com.example.KinetoWebsite.Service.RecaptchaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// IMPORTUL NOU
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // INLOCUIRE @MockBean CU @MockitoBean PENTRU TOATE DEPENDINȚELE
    @MockitoBean
    private AppointmentService appointmentService;

    @MockitoBean
    private RecaptchaService recaptchaService;

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    private AppointmentDTO appointmentDTO;

    @BeforeEach
    void setUp() {
        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(1L);
        appointmentDTO.setPatientName("Ion Popescu");
        appointmentDTO.setCustomerEmail("ion@test.com");
        appointmentDTO.setDate(LocalDate.now());
    }

    @Test
    void getAllAppointments_ShouldReturnList() throws Exception {
        when(appointmentService.getAllAppointments()).thenReturn(Arrays.asList(appointmentDTO));

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientName", is("Ion Popescu")));
    }

    @Test
    void getAppointmentById_WhenExists_ShouldReturnAppointment() throws Exception {
        when(appointmentService.getAppointmentById(1L)).thenReturn(Optional.of(appointmentDTO));

        mockMvc.perform(get("/api/appointments/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientName", is("Ion Popescu")));
    }

    @Test
    void getAppointmentById_WhenNotExists_ShouldReturn404() throws Exception {
        when(appointmentService.getAppointmentById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/appointments/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAppointment_Success_WithEmailSending() throws Exception {
        when(appointmentService.createAppointment(any(AppointmentDTO.class))).thenReturn(appointmentDTO);
        doNothing().when(emailService).processAppointment(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientName", is("Ion Popescu")));

        verify(emailService, times(1)).processAppointment(eq("ion@test.com"), eq("Ion Popescu"), anyString());
    }

    @Test
    void createAppointment_Success_NoEmail_ShouldNotSendEmail() throws Exception {
        appointmentDTO.setCustomerEmail(null);
        when(appointmentService.createAppointment(any(AppointmentDTO.class))).thenReturn(appointmentDTO);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDTO)))
                .andExpect(status().isOk());

        verify(emailService, never()).processAppointment(any(), any(), any());
    }

    @Test
    void createAppointment_Failure_ShouldReturnBadRequest() throws Exception {
        when(appointmentService.createAppointment(any(AppointmentDTO.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Appointment creation failed")))
                .andExpect(jsonPath("$.message", containsString("Database error")));
    }

    @Test
    void updateAppointment_ShouldReturnUpdated() throws Exception {
        when(appointmentService.updateAppointment(eq(1L), any(AppointmentDTO.class))).thenReturn(appointmentDTO);

        mockMvc.perform(put("/api/appointments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientName", is("Ion Popescu")));
    }

    @Test
    void deleteAppointment_ShouldReturnNoContent() throws Exception {
        doNothing().when(appointmentService).deleteAppointment(1L);

        mockMvc.perform(delete("/api/appointments/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(appointmentService, times(1)).deleteAppointment(1L);
    }
}
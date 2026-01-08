package com.example.KinetoWebsite.Controller;

import com.example.KinetoWebsite.Service.AppointmentService;
import com.example.KinetoWebsite.Service.ServiceDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final AppointmentService appointmentService;
    private final ServiceDetailsService serviceDetailsService;

    public ViewController(AppointmentService appointmentService,
                          ServiceDetailsService serviceDetailsService) {
        this.appointmentService = appointmentService;
        this.serviceDetailsService = serviceDetailsService;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        // Add statistics data to the model
        int totalServices = serviceDetailsService.getAllServices().size();
        int totalAppointments = appointmentService.getAllAppointments().size();

        model.addAttribute("totalServices", totalServices);
        model.addAttribute("totalAppointments", totalAppointments);
        model.addAttribute("pageTitle", "Dashboard Admin - Kineto");
        // ADDED: Set current page for sidebar highlighting
        model.addAttribute("currentPage", "dashboard");
        return "AdminDashboard";
    }

    @GetMapping("/admin/appointments")
    public String appointmentsManagement(Model model) {
        // Appointments will be loaded via JavaScript from API
        model.addAttribute("pageTitle", "Gestionare Programări - Kineto");
        // ADDED: Set current page for sidebar highlighting (Fixes 500 error)
        model.addAttribute("currentPage", "appointments");
        return "AppointmentsManagement";
    }

    @GetMapping("/admin/services")
    public String serviceManagement(Model model) {
        // Services will be loaded via JavaScript from API
        model.addAttribute("pageTitle", "Gestionare Servicii - Kineto");
        // ADDED: Set current page for sidebar highlighting
        model.addAttribute("currentPage", "services");
        return "ServiceManagement";
    }

    @GetMapping("/admin")
    public String admin() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "AdminLogin";
    }

    @GetMapping("/")
    public String publicPage(Model model) {
        model.addAttribute("pageTitle", "Kineto - Masaj Terapeutic la Domiciliu");
        return "PublicPage";
    }
}
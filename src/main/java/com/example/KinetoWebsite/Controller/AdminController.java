package com.example.KinetoWebsite.Controller;

import com.example.KinetoWebsite.Model.DTO.ServiceDetailsDTO;
import com.example.KinetoWebsite.Service.ServiceDetailsService;
import com.example.KinetoWebsite.Service.ServiceDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AdminController {

    private final ServiceDetailsService serviceDetailsService;

    public AdminController(ServiceDetailsService serviceDetailsService){
        this.serviceDetailsService = serviceDetailsService;
    }

    //Admin-only endpoints.

    @PostMapping("/services")
    public ResponseEntity<ServiceDetailsDTO> createService(@RequestBody ServiceDetailsDTO serviceDetailsDTO){
        ServiceDetailsDTO createdService = serviceDetailsService.createService(serviceDetailsDTO);

        return ResponseEntity.ok(createdService);
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<ServiceDetailsDTO> updateService(@PathVariable Long id,
                                                           @RequestBody ServiceDetailsDTO serviceDetailsDTO){
        ServiceDetailsDTO updateService = serviceDetailsService.updateService(id, serviceDetailsDTO);
        return ResponseEntity.ok(updateService);
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id){
        serviceDetailsService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/services")
    public List<ServiceDetailsDTO> getAllServicesAdmin(){
        return serviceDetailsService.getAllServices();
    }

}

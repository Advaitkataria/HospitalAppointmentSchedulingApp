package org.example.hospitalappointmentschedulingapp.controller;

import jakarta.validation.Valid;
import org.example.hospitalappointmentschedulingapp.model.Appointment;
import org.example.hospitalappointmentschedulingapp.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private AppointmentService appointmentService;
    @Autowired
    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService=appointmentService;
    }

    @GetMapping()
    ResponseEntity<List<Appointment>> getAllAppointments(){
        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    ResponseEntity<Appointment> updateAppointment(@RequestBody Appointment appointment,@PathVariable int id){
        return new ResponseEntity<>(appointmentService.updateAppointment(appointment,id),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteAppointment(@PathVariable int id){
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping()
    ResponseEntity<Appointment> addAppointment(@RequestBody @Valid Appointment appointment){
        return new ResponseEntity<>(appointmentService.addAppointment(appointment),HttpStatus.CREATED);
    }
}


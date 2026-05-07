package org.example.hospitalappointmentschedulingapp.controller;

import jakarta.validation.Valid;
import org.example.hospitalappointmentschedulingapp.model.Visit;
import org.example.hospitalappointmentschedulingapp.repository.VisitRepository;
import org.example.hospitalappointmentschedulingapp.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment/{appointmentId}/visit")
public class VisitController {

    private VisitService visitService;
    @Autowired
    public VisitController(VisitService visitService){
        this.visitService=visitService;
    }

    @GetMapping
    ResponseEntity<List<Visit>> getAllVisitsByAppointment(@PathVariable Integer appointmentId){
        return new ResponseEntity<>(visitService.getVisitsByAppointment(appointmentId), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Visit> addVisit(@PathVariable int appointmentId,@RequestBody @Valid Visit visit){
        return new ResponseEntity<>(visitService.addVisit(visit,appointmentId),HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    ResponseEntity<Visit> updateVisit(@PathVariable int id,@RequestBody Visit visit){
        return new ResponseEntity<>(visitService.updateVisit(id, visit),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteVisit(@PathVariable int id){
        visitService.deleteVisit(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

package org.example.hospitalappointmentschedulingapp.service;

import org.example.hospitalappointmentschedulingapp.model.Appointment;
import org.example.hospitalappointmentschedulingapp.model.User;
import org.example.hospitalappointmentschedulingapp.model.Visit;
import org.example.hospitalappointmentschedulingapp.repository.AppointmentRepository;
import org.example.hospitalappointmentschedulingapp.repository.UserRepository;
import org.example.hospitalappointmentschedulingapp.repository.VisitRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitService {
    private UserRepository userRepository;
    private AppointmentRepository appointmentRepository;
    private VisitRepository visitRepository;

    public VisitService(UserRepository userRepository, AppointmentRepository appointmentRepository,VisitRepository visitRepository){
        this.userRepository=userRepository;
        this.appointmentRepository=appointmentRepository;
        this.visitRepository=visitRepository;
    }

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User does not exist"));
    }

    private Appointment getAppointmentAndVerifyOwner(Integer appointmentId){
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(()->new RuntimeException("Appointment does not exist"));
        if(!appointment.getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new RuntimeException("You can access only your appointments");
        }
        return appointment;
    }

    public List<Visit> getVisitsByAppointment(Integer appointmentId){
        getAppointmentAndVerifyOwner(appointmentId);
        return visitRepository.findByAppointmentId(appointmentId);
    }

    public Visit addVisit(Visit visit,Integer appointmentId){
        Appointment appointment=getAppointmentAndVerifyOwner(appointmentId);
        visit.setAppointment(appointment);
        return visitRepository.save(visit);
    }

    public void deleteVisit(int id){
        Visit visit = visitRepository.findById(id).orElseThrow(()->new RuntimeException("Visit does not exist"));
        if(!visit.getAppointment().getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new RuntimeException("You can access only your visits");
        }
        visitRepository.deleteById(id);
    }
    public Visit updateVisit(int id, Visit updatedVisit){
        Visit visit = visitRepository.findById(id).orElseThrow(()->new RuntimeException("Visit does not exist"));
        if(!visit.getAppointment().getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new RuntimeException("You can access only your visits");
        }

        visit.setDuration(updatedVisit.getDuration());
        visit.setPrice(updatedVisit.getPrice());
        visit.setNotes(updatedVisit.getNotes());

        return visitRepository.save(visit);
    }

}

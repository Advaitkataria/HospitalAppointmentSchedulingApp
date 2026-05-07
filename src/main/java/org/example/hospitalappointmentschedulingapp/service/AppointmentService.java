package org.example.hospitalappointmentschedulingapp.service;

import org.example.hospitalappointmentschedulingapp.model.Appointment;
import org.example.hospitalappointmentschedulingapp.model.User;
import org.example.hospitalappointmentschedulingapp.repository.AppointmentRepository;
import org.example.hospitalappointmentschedulingapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    private AppointmentRepository appointmentRepository;
    private UserRepository userRepository;


    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository){
        this.appointmentRepository=appointmentRepository;
        this.userRepository=userRepository;
    }
    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User does not exist"));
    }

    public List<Appointment> getAllAppointments(){
        String email = getCurrentUser().getEmail();
        return appointmentRepository.findByUserEmail(email);
    }

    public Appointment addAppointment(Appointment appointment){
        appointment.setUser(getCurrentUser());
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(int id){
        Appointment appointment=appointmentRepository.findById(id).orElseThrow(()->new RuntimeException("Appointment not found"));
        if(!appointment.getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new RuntimeException("You can access only your appointments");
        }
        appointmentRepository.deleteById(id);
    }

    public Appointment updateAppointment(Appointment updatedAppointment,int id){
        Appointment appointment =appointmentRepository.findById(id).orElseThrow(()->new RuntimeException("Appointment not found"));
        if(!appointment.getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new RuntimeException("You can access only your appointments");
        }
        appointment.setService(updatedAppointment.getService());
        appointment.setDoctor(updatedAppointment.getDoctor());
        appointment.setDateTime(updatedAppointment.getDateTime());
        return appointmentRepository.save(appointment);
    }

}

package org.example.hospitalappointmentschedulingapp.repository;

import org.example.hospitalappointmentschedulingapp.model.Appointment;
import org.example.hospitalappointmentschedulingapp.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit,Integer> {

    List<Visit> findByAppointmentId(Integer appointmentId);
}

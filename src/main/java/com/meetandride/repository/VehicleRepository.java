package com.meetandride.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meetandride.model.User;
import com.meetandride.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByOwner(User owner);
}

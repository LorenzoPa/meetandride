package com.meetandride.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meetandride.model.User;
import com.meetandride.model.Vehicle;
import com.meetandride.repository.VehicleRepository;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void save(Vehicle vehicle) { vehicleRepository.save(vehicle); }

    public List<Vehicle> findByOwner(User owner) {
        return vehicleRepository.findByOwner(owner);
    }
}

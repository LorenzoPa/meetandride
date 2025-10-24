package com.meetandride.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meetandride.model.User;
import com.meetandride.model.Vehicle;
import com.meetandride.repository.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * 🔹 Crea o aggiorna il veicolo associato a un utente.
     * Se l’utente possiede già un veicolo, viene aggiornato lo stesso record.
     */
    @Transactional
    public Vehicle saveOrUpdateFor(User owner, Vehicle newVehicle) {
        Optional<Vehicle> existing = vehicleRepository.findByOwner(owner);

        // Controllo targa duplicata (solo se appartiene a un altro utente)
        boolean targaEsistente = vehicleRepository.findByTargaIgnoreCase(newVehicle.getTarga())
                .filter(v -> !v.getOwner().getId().equals(owner.getId()))
                .isPresent();

        if (targaEsistente) {
            throw new IllegalArgumentException("La targa è già registrata da un altro utente.");
        }

        // Se l’utente ha già un veicolo → aggiorna stesso ID (evita duplicati)
        existing.ifPresent(oldVehicle -> newVehicle.setId(oldVehicle.getId()));

        newVehicle.setOwner(owner);
        return vehicleRepository.save(newVehicle);
    }

    /**
     * 🔹 Recupera il veicolo associato a un utente.
     */
    @Transactional(readOnly = true)
    public Optional<Vehicle> findByOwner(User owner) {
        return vehicleRepository.findByOwner(owner);
    }

    /**
     * 🔹 Elimina il veicolo associato a un utente.
     */
    @Transactional
    public void deleteFor(User owner) {
        vehicleRepository.findByOwner(owner)
                .ifPresent(vehicleRepository::delete);
    }
}

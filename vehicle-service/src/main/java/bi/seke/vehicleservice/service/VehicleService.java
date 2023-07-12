package bi.seke.vehicleservice.service;

import bi.seke.vehicleservice.entities.VehicleEntity;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    /**
     * Gets all vehicles
     *
     * @return a list of all vehicles
     */
    List<VehicleEntity> getVehicles();

    /**
     * Get vehicle for vehicle plate number
     *
     * @param plateNumber
     * @return vehicle
     */
    Optional<VehicleEntity> getVehicleForPlateNumber(String plateNumber);
}

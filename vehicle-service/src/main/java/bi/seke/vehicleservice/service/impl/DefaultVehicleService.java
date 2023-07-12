package bi.seke.vehicleservice.service.impl;

import bi.seke.vehicleservice.VehicleRepository;
import bi.seke.vehicleservice.entities.VehicleEntity;
import bi.seke.vehicleservice.service.VehicleService;

import java.util.List;
import java.util.Optional;

public class DefaultVehicleService implements VehicleService {
    protected final VehicleRepository vehicleRepository;

    public DefaultVehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<VehicleEntity> getVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Optional<VehicleEntity> getVehicleForPlateNumber(final String plateNumber) {
        return vehicleRepository.findByPlateNumber(plateNumber);
    }
}

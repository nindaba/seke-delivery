package bi.seke.vehicleservice.repositories;

import bi.seke.vehicleservice.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
}

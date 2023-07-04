package bi.seke.vehicleservice.facades;


import bi.seke.data.VehicleData;

import java.util.List;
import java.util.Optional;

public interface VehicleFacade {
    List<VehicleData> getVehicles();

    Optional<VehicleData> getVehicleForUid(String vehicleUid);
}

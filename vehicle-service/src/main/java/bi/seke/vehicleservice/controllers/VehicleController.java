package bi.seke.vehicleservice.controllers;

import bi.seke.data.VehicleData;
import bi.seke.vehicleservice.facades.VehicleFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("vehicles")
public class VehicleController {
    protected final VehicleFacade vehicleFacade;

    public VehicleController(VehicleFacade vehicleFacade) {
        this.vehicleFacade = vehicleFacade;
    }

    @GetMapping
    public ResponseEntity<List<VehicleData>> getAllVehicles() {
        return ResponseEntity.ok(vehicleFacade.getVehicles());
    }

    @GetMapping("/{vehicleUid}")
    public ResponseEntity<VehicleData> getVehicle(@PathVariable() String vehicleUid) {
        return ResponseEntity.of(vehicleFacade.getVehicleForUid(vehicleUid));
    }
}

package bi.seke.vehicleservice.facades.impl;

import bi.seke.data.VehicleData;
import bi.seke.vehicleservice.facades.VehicleFacade;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component("vehicleFacade")
public class DefaultVehicleFacade implements VehicleFacade {
    @Override
    public List<VehicleData> getVehicles() {
        return List.of(
                createDeliveryCar("Ford Transit", "DT123456"),
                createDeliveryCar("Mercedes-Benz Sprinter", "MB789012"),
                createDeliveryCar("Volkswagen Transporter", "VW345678"),
                createDeliveryCar("Renault Kangoo", "RK901234"),
                createDeliveryCar("Peugeot Partner", "PP567890"),
                createDeliveryCar("Citroën Berlingo", "CB123456"),
                createDeliveryCar("Fiat Ducato", "FD789012"),
                createDeliveryCar("Nissan NV200", "NN345678"),
                createDeliveryCar("Toyota Hiace", "TH901234"),
                createDeliveryCar("Chevrolet Express", "CE567890")
        );
    }

    @Override
    public Optional<VehicleData> getVehicleForUid(final String vehicleUid) {
        return getVehicles().stream()
                .filter(vehicle -> Objects.equals(vehicle.getName(), vehicleUid))
                .findFirst();
    }

    protected VehicleData createDeliveryCar(final String name, final String uid) {
        VehicleData car = new VehicleData();
        car.setName(name);
        car.setUid(uid);
        return car;
    }
}

package bi.seke.pricingservice.controllers;

import bi.seke.pricingservice.services.PriceConfigurationService;
import bi.seke.schema.deliveryservice.PackageDTO;
import bi.seke.schema.pricingservice.PriceDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PriceController {
    protected final PriceConfigurationService service;

    @PostMapping("/calculate")
    public ResponseEntity<PriceDTO> calculatePrice(@RequestBody final PackageDTO packag) {
        return ResponseEntity.ok(service.calculatePackagePrice(packag));
    }
}

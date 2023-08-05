package bi.seke.pricingservice.controllers;

import bi.seke.pricingservice.services.PriceService;
import bi.seke.schema.deliveryservice.PackageDTO;
import bi.seke.schema.pricingservice.PriceDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class PriceController extends BaseController {
    protected final PriceService service;

    @PostMapping("/calculate")
    public ResponseEntity<PriceDTO> calculatePrice(@RequestBody final PackageDTO packag) {
        return ResponseEntity.ok(service.calculatePackagePrice(packag));
    }

    @GetMapping("/package/{packageUid}")
    public ResponseEntity<PriceDTO> getPrice(@PathVariable final String packageUid) {
        return ResponseEntity.ok(service.getDefinitePrice(packageUid));
    }
}

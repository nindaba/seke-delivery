package bi.seke.paymentservice.controllers;

import bi.seke.paymentservice.services.PriceService;
import bi.seke.schema.paymentservice.PaymentDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/confirm")
public class PaymentController {
    protected final PriceService service;

    @PostMapping
    void confirmPayment(@RequestBody final PaymentDTO payment) {
        service.confirmPayment(payment);
    }
}

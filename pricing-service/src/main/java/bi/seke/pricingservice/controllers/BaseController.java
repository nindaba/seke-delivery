package bi.seke.pricingservice.controllers;

import bi.seke.pricingservice.exceptions.PriceNotFoundException;
import bi.seke.schema.common.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseController {
    @ExceptionHandler({PriceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handlePriceNotFound(final HttpServletRequest request, final PriceNotFoundException ex) {
        return new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage());
    }
}

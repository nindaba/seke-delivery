package bi.seke.deliveryservice.validators;

import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collection;

@Log4j2
public class GlobalValidator {
    public static <SOURCE> boolean validate(final SOURCE source, final Collection<Validator> validators) {
        final Errors errors = new BindException(source, source.getClass().getName());

        validators.stream()
                .filter(validator -> validator.supports(source.getClass()))
                .forEach(validator -> validator.validate(source, errors));

        if (errors.getGlobalErrorCount() > 0) {
            log.error("{} validation failed due to {}", errors.getObjectName(), errors.getGlobalError().getCode());
            return false;
        }

        errors.getAllErrors()
                .forEach(error -> {
                    log.warn(error.getCode());
                });
        return true;
    }
}

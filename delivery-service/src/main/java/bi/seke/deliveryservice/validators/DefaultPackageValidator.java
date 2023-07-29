package bi.seke.deliveryservice.validators;

import bi.seke.data.dtos.PackageDTO;
import bi.seke.deliveryservice.configurations.Configurations;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("packageValidator")
@AllArgsConstructor
public class DefaultPackageValidator implements Validator {
    public static final String WEIGHT_LESS_MIN = "weight.less.min";
    public static final String WEIGHT_EXCEEDS_MAX = "weight.exceeds.max";
    public static final String MISSING_TO_ADDRESS = "missing.to.address";
    public static final String MISSING_FROM_ADDRESS = "missing.from.address";

    protected final Configurations configurations;

    @Override
    public boolean supports(Class<?> clazz) {
        return PackageDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof final PackageDTO packag) {
            validateAddresses(packag, errors);
            validateWeight(packag.getWeight(), errors);
        }

    }

    protected void validateAddresses(final PackageDTO packag, final Errors errors) {
        if (StringUtils.isBlank(packag.getFrom())) {
            errors.rejectValue("from", MISSING_FROM_ADDRESS);
        }

        if (StringUtils.isBlank(packag.getTo())) {
            errors.rejectValue("to", MISSING_TO_ADDRESS);
        }
    }

    protected void validateWeight(final double weight, final Errors errors) {
        if (weight > configurations.getPackageMaxWeight()) {
            errors.rejectValue("weight", WEIGHT_EXCEEDS_MAX);
        }

        if (weight != 0 && weight < configurations.getPackageMinWeight()) {
            errors.reject(WEIGHT_LESS_MIN);
        }
    }
}

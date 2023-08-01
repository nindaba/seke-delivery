package bi.seke.deliveryservice.validators;

import bi.seke.data.dtos.PackageDTO;
import bi.seke.deliveryservice.configurations.Configurations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class DefaultPackageValidatorTest {
    PackageDTO PACKAGE = new PackageDTO();
    @Mock
    Configurations configurations;
    @InjectMocks
    DefaultPackageValidator validator;

    @Test
    void validateAddresses() {
        Errors errors = createErrors();
        validator.validateAddresses(PACKAGE, errors);
        assertEquals(2, errors.getErrorCount());

        errors = createErrors();
        PACKAGE.setTo("arthur");
        validator.validateAddresses(PACKAGE, errors);
        assertEquals(1, errors.getErrorCount());

        errors = createErrors();
        PACKAGE.setFrom("arthur");
        validator.validateAddresses(PACKAGE, errors);
        assertEquals(0, errors.getErrorCount());


    }

    @Test
    void validateWeight() {
        when(configurations.getPackageMaxWeight()).thenReturn(50d);
        when(configurations.getPackageMinWeight()).thenReturn(.1);

        Errors errors = createErrors();
        validator.validateWeight(-1, errors);
        assertEquals(1, errors.getErrorCount());

        errors = createErrors();
        validator.validateWeight(0, errors);
        assertEquals(0, errors.getErrorCount());

        errors = createErrors();
        validator.validateWeight(.1, errors);
        assertEquals(0, errors.getErrorCount());

        errors = createErrors();
        validator.validateWeight(5, errors);
        assertEquals(0, errors.getErrorCount());

        errors = createErrors();
        validator.validateWeight(50.1, errors);
        assertEquals(1, errors.getErrorCount());


    }

    private BindException createErrors() {
        return new BindException(PACKAGE, "package");
    }
}

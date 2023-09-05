package bi.seke.schema.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class AddressDTO implements Serializable {
    private String code;
    private String firstName;
    private String lastName;
    private String postCode;
    private String line1;
    private String line2;
    private String country;
    private String email;
    private String phone;
}

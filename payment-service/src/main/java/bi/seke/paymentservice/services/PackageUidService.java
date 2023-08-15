package bi.seke.paymentservice.services;

import java.util.UUID;

public interface PackageUidService {

    String DELIMITER = "_";

    /**
     * Decode the package uid, if the decoding fails and the creation of the PK succeed we assume that<br>
     * the package uid was passed was the customer uid of type {@link UUID}<br>
     * maybe to be changed in the version 1.1
     *
     * @param packageUid
     * @return packagePK
     */
    String decodeCustomerUid(final String packageUid);

}

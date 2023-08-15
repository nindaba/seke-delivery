package bi.seke.paymentservice.services.impl;

import bi.seke.paymentservice.exceptions.InvalidPackageUidException;
import bi.seke.paymentservice.services.PackageUidService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Base64;


@Service("packageUidService")
public class DefaultPackageUidService implements PackageUidService {

    @Override
    public String decodeCustomerUid(String packageUid) {
        Assert.notNull(packageUid, "Package uid can not be null");

        try {
            final String decoded = new String(Base64.getDecoder().decode(packageUid));
            final String[] s = StringUtils.split(decoded, DELIMITER);
            if (s.length != 2) {
                throw new InvalidPackageUidException("Invalid package uid, incorrect parts");
            }
            return s[1];
        } catch (Exception e) {
            throw new InvalidPackageUidException(e);
        }
    }
}

package bi.seke.deliveryservice.services;

import bi.seke.deliveryservice.entities.PackagePK;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.*;

public interface PackagePKService {

    String PACKAGE_UID_FORMAT = "%s_%s";
    String DELIMITER = "_";

    default String encode(final PackagePK pk) {
        Assert.notNull(pk, "Package pk can not be null");
        Assert.notNull(pk.getUid(), "Package pk can not be null");
        Assert.notNull(pk.getCustomerUid(), "Package pk can not be null");

        final String formatted = PACKAGE_UID_FORMAT.formatted(pk.getUid(), pk.getCustomerUid());
        return Base64.getEncoder().encodeToString(formatted.getBytes());
    }

    default PackagePK decode(final String packageUid) {
        final String decoded = new String(Base64.getDecoder().decode(packageUid));
        final String[] s = StringUtils.split(decoded, DELIMITER);

        Assert.isTrue(s.length == 2, "Invalid packageUid %s".formatted(packageUid));
        return createPK(s);
    }

    default PackagePK createPK() {
        final PackagePK pk = new PackagePK();
        pk.setUid(Uuids.timeBased());
        pk.setCreatedAt(new Date());
        pk.setModifiedAt(new Date());
        return pk;
    }

    default PackagePK createPK(final String uid, final String customerUid) {
        final PackagePK pk = new PackagePK();
        pk.setCustomerUid(UUID.fromString(customerUid));
        pk.setUid(UUID.fromString(uid));
        return pk;
    }

    private PackagePK createPK(final String... uids) {
        final PackagePK pk = new PackagePK();
        final Iterator<String> iterator = Arrays.stream(uids).iterator();
        if (iterator.hasNext()) {
            pk.setUid(UUID.fromString(iterator.next()));
        }
        if (iterator.hasNext()) {
            pk.setCustomerUid(UUID.fromString(iterator.next()));
        }
        return pk;
    }
}

package bi.seke.deliveryservice.services;

import bi.seke.deliveryservice.entities.PackagePK;
import bi.seke.deliveryservice.services.impl.DefaultPackagePkService;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PackagePKServiceTest {

    PackagePKService packagePkService = new DefaultPackagePkService();

    static PackagePK PACKAGE_PK() {
        PackagePK pk = new PackagePK();
        pk.setUid(UUID.fromString("b8fe688d-937e-4c3b-b689-d1b4d3a3fd00"));
        pk.setCustomerUid(UUID.fromString("1dd5d536-b6e5-462d-8df1-89f687fd6d6a"));
        pk.setCreatedAt(new Date());
        pk.setModifiedAt(new Date());
        return pk;
    }

    static String PACKAGE_UID() {
        return Base64.getEncoder().encodeToString("%s_%s".formatted(PACKAGE_PK().getUid(), PACKAGE_PK().getCustomerUid()).getBytes());
    }

    @Test
    void encode() {
        String actual = packagePkService.encode(PACKAGE_PK());
        assertEquals(PACKAGE_UID(), actual);
    }


    @Test
    void decode() {
        PackagePK pk = packagePkService.decode(PACKAGE_UID());
        assertEquals(PACKAGE_PK().getUid(), pk.getUid());
        assertEquals(PACKAGE_PK().getCustomerUid(), pk.getCustomerUid());
    }

    @Test
    void createPK() {
        PackagePK pk = packagePkService.createPK();
        assertNull(pk.getCustomerUid());
        assertNotNull(pk.getUid());
        assertNotNull(pk.getCreatedAt());
        assertNotNull(pk.getModifiedAt());
    }
}

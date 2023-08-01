package bi.seke.deliveryservice.services.impl;

import bi.seke.data.dtos.PackageDTO;
import bi.seke.deliveryservice.configurations.Configurations;
import bi.seke.deliveryservice.repositories.PackageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.Validator;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class DefaultPackageServiceTest {

    String PACKAGE_UID = "4591624f-9b52-42b3-9afa-64d7cc64e075";
    PackageDTO PACKAGE = createPackage();
    PackageDTO CACHED_PACKAGE = createPackage();

    @Mock
    BoundValueOperations<String, PackageDTO> boundValueOperations;

    @InjectMocks
    DefaultPackageService service;

    @Mock
    PackageRepository packageRepository;
    @Mock
    RedisTemplate<String, PackageDTO> redisTemplate;
    @Mock
    KafkaTemplate<String, PackageDTO> kafkaTemplate;
    @Mock
    Configurations configurations;

    @Mock
    Collection<Validator> validators;

    @BeforeEach
    void setUp() {
        when(configurations.getPackageCacheName()).thenReturn("A");
        String KEY = Configurations.CACHE_KEY_FORMATTER.formatted(configurations.getPackageCacheName(), PACKAGE_UID);

        when(redisTemplate.boundValueOps(KEY)).thenReturn(boundValueOperations);
        when(kafkaTemplate.send(any(), any(), any())).thenReturn(new CompletableFuture<>());

    }

    @Test
    void assemble() {
        when(boundValueOperations.get()).thenReturn(null);
        PackageDTO ACTUAL = service.assemble(PACKAGE);
        assertEquals(ACTUAL, PACKAGE);

        CACHED_PACKAGE.setFragile(true);
        when(boundValueOperations.get()).thenReturn(CACHED_PACKAGE);
        ACTUAL = service.assemble(PACKAGE);
        assertTrue(ACTUAL.getFragile());
    }

    PackageDTO createPackage() {
        PackageDTO packageDTO = new PackageDTO();
        packageDTO.setPackageUid(PACKAGE_UID);
        return packageDTO;
    }
}

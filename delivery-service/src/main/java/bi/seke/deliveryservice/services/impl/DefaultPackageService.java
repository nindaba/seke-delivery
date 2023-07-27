package bi.seke.deliveryservice.services.impl;

import bi.seke.deliveryservice.configurations.Configurations;
import bi.seke.deliveryservice.dtos.PackageDTO;
import bi.seke.deliveryservice.entities.PackageEntity;
import bi.seke.deliveryservice.entities.PackagePK;
import bi.seke.deliveryservice.exceptions.WritingException;
import bi.seke.deliveryservice.repositories.PackageRepository;
import bi.seke.deliveryservice.services.PackageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static bi.seke.deliveryservice.configurations.Configurations.CACHE_KEY_FORMATTER;

@AllArgsConstructor
@Service("packageService")
@Log4j2
public class DefaultPackageService implements PackageService {
    protected final PackageRepository packageRepository;
    protected final RedisTemplate<String, PackageDTO> redisTemplate;
    protected final KafkaTemplate<String, PackageDTO> kafkaTemplate;
    protected final Configurations configurations;

    @Override
    public List<PackageEntity> getPackagesByCustomerUid(final String customerUid) {
        final UUID customer = UUID.fromString(customerUid);
        return packageRepository.findAllByPkCustomerUid(customer);
    }

    @Override
    public List<PackageEntity> getAllPackages() {
        return packageRepository.findAll();
    }

    @Override
    public void deletePackagesByCustomerUid(final String customerUid) {
        packageRepository.deleteAllByPkCustomerUid(UUID.fromString(customerUid));
    }

    @Override
    public void deleteAllPackages() {
        packageRepository.deleteAll();
    }

    @Override
    public Optional<PackageEntity> getPackageByUid(PackagePK packagePK) {
        return packageRepository.findById(packagePK);
    }

    @Override
    public void deleteByPk(PackagePK pk) {
        packageRepository.deleteById(pk);
    }

    @Override
    public PackageDTO assemble(final PackageDTO source) {
        Assert.notNull(source, "PackageDTO must not be null");
        final String key = CACHE_KEY_FORMATTER.formatted(configurations.getPackageCacheName(), source.getPackageUid());

        Assert.notNull(source, "PackageDTO.packageUid must not be null");

        final BoundValueOperations<String, PackageDTO> valueOps = redisTemplate.boundValueOps(key);

        return Optional.ofNullable(valueOps.get())
                .map(packag -> {
                    BeanUtils.copyProperties(source, packag);
                    return packag;
                })
                .or(() -> Optional.of(source))
                .filter(this::validatedPackage)
                .map(this::sendToPackageTopic)
                .orElse(source);
    }

    private PackageDTO sendToPackageTopic(final PackageDTO target) {
        final CompletableFuture<SendResult<String, PackageDTO>> send = kafkaTemplate.send(
                configurations.getPackageTopicName(), target.getPackageUid(), target);

        send.whenComplete(this::logKafkaResults);
        return target;
    }

    private Boolean validatedPackage(final PackageDTO packageDTO) {
        return Optional.of(packageDTO)
                .filter(packag -> StringUtils.isNotBlank(packag.getPackageUid()))
                .filter(packag -> validateWeight(packag)
                        || StringUtils.isNotBlank(packag.getFrom() + packag.getTo()))
                .isPresent();
    }

    private boolean validateWeight(final PackageDTO packag) {
        return packag.getWeight() >= configurations.getPackageMinWeight()
                && packag.getWeight() <= configurations.getPackageMaxWeight();
    }

    protected void logKafkaResults(final SendResult<String, PackageDTO> results, final Throwable throwable) {
        if (throwable != null) {
            log.error("Could not send the Package to kafka topic", throwable);
            throw new WritingException(throwable);
        }

        log.debug("Sent Package {} to {}", results.getProducerRecord().key(), results.getRecordMetadata().topic());
    }
}

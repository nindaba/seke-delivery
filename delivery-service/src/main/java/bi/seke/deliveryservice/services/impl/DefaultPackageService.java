package bi.seke.deliveryservice.services.impl;

import bi.seke.data.dtos.PackageDTO;
import bi.seke.deliveryservice.configurations.Configurations;
import bi.seke.deliveryservice.entities.PackageEntity;
import bi.seke.deliveryservice.entities.PackagePK;
import bi.seke.deliveryservice.exceptions.WritingException;
import bi.seke.deliveryservice.repositories.PackageRepository;
import bi.seke.deliveryservice.services.PackageService;
import bi.seke.deliveryservice.validators.GlobalValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;

import java.util.Collection;
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
    protected final Collection<Validator> packageValidators;

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
                .map(target -> mergePackage(source, target))
                .or(() -> Optional.of(source))
                .filter(packag -> GlobalValidator.validate(packag, packageValidators))
                .map(this::sendToPackageTopic)
                .orElse(source);
    }

    private PackageDTO mergePackage(final PackageDTO source, final PackageDTO target) {
        Optional.ofNullable(source.getRoutes())
                .ifPresent(target::setRoutes);

        Optional.ofNullable(source.getWeight())
                .ifPresent(target::setWeight);

        Optional.ofNullable(source.getVolume())
                .ifPresent(target::setVolume);

        Optional.ofNullable(source.getFragile())
                .ifPresent(target::setFragile);

        Optional.ofNullable(source.getEmail())
                .ifPresent(target::setEmail);

        Optional.ofNullable(source.getPhone())
                .ifPresent(target::setPhone);

        Optional.ofNullable(source.getDeliveryType())
                .ifPresent(target::setDeliveryType);

        Optional.ofNullable(source.getDescription())
                .ifPresent(target::setDescription);

        Optional.ofNullable(source.getCreatedB())
                .ifPresent(target::setCreatedB);

        Optional.ofNullable(source.getFrom())
                .ifPresent(target::setFrom);

        Optional.ofNullable(source.getTo())
                .ifPresent(target::setTo);

        return target;
    }

    protected PackageDTO sendToPackageTopic(final PackageDTO target) {
        final CompletableFuture<SendResult<String, PackageDTO>> send = kafkaTemplate.send(
                configurations.getPackageTopicName(), target.getPackageUid(), target);

        send.whenComplete(this::logKafkaResults);
        return target;
    }

    protected void logKafkaResults(final SendResult<String, PackageDTO> results, final Throwable throwable) {
        if (throwable != null) {
            log.error("Could not send the Package to kafka topic", throwable);
            throw new WritingException(throwable);
        }

        log.debug("Sent Package {} to {}", results.getProducerRecord().key(), results.getRecordMetadata().topic());
    }
}

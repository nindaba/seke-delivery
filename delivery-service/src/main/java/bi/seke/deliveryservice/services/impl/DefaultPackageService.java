package bi.seke.deliveryservice.services.impl;

import bi.seke.deliveryservice.entities.PackageEntity;
import bi.seke.deliveryservice.entities.PackagePK;
import bi.seke.deliveryservice.repositories.PackageRepository;
import bi.seke.deliveryservice.services.PackageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service("packageService")
@Log4j2
public class DefaultPackageService implements PackageService {
    protected final PackageRepository packageRepository;

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
}

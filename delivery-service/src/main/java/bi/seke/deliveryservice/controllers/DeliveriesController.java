package bi.seke.deliveryservice.controllers;

import bi.seke.deliveryservice.dtos.PackageDTO;
import bi.seke.deliveryservice.entities.PackageEntity;
import bi.seke.deliveryservice.entities.PackagePK;
import bi.seke.deliveryservice.exceptions.MappingException;
import bi.seke.deliveryservice.exceptions.PackageMissingUid;
import bi.seke.deliveryservice.mappers.strategies.MapperStrategy;
import bi.seke.deliveryservice.services.PackagePKService;
import bi.seke.deliveryservice.services.PackageService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
@RestController
public class DeliveriesController {
    public static final String PACKAGES_CACHE = "Packages";
    public static final String PACKAGE_KEY_GENERATOR = "packageKeyGenerator";

    protected final PackageService packageService;
    protected final MapperStrategy mapperStrategy;
    protected final PackagePKService packagePKService;

    @RequestMapping(value = "customers/{customer-uid}", method = RequestMethod.GET)
    @Cacheable(value = PACKAGES_CACHE, keyGenerator = PACKAGE_KEY_GENERATOR)
    public Collection<PackageDTO> getCustomerPackages(@PathVariable("customer-uid") final String customerUid) throws Exception {
        final List<PackageEntity> packages = packageService.getPackagesByCustomerUid(customerUid);
        return mapperStrategy.doMap(packages, PackageDTO.class);
    }

    @RequestMapping(value = "/{package-uid}", method = RequestMethod.GET)
    @Cacheable(value = PACKAGES_CACHE, keyGenerator = PACKAGE_KEY_GENERATOR)
    public PackageDTO getPackageByUid(@PathVariable("package-uid") final String packageUid) throws Exception {
        final PackagePK pk = packagePKService.decode(packageUid);

        return packageService.getPackageByUid(pk)
                .map(getPackageMapper())
                .orElse(null);
    }

    @RequestMapping(method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.CREATED)
    @CachePut(value = PACKAGES_CACHE, keyGenerator = PACKAGE_KEY_GENERATOR)
    public PackageDTO createOrUpdate(@RequestBody final PackageDTO packag) throws PackageMissingUid {

        if (packag.getPackageUid() != null) {
            final PackagePK pk = packagePKService.decode(packag.getPackageUid());
            packag.setPackageUid(packagePKService.encode(pk));
            return packag;
        }

        throw new PackageMissingUid();
    }

    @RequestMapping(method = RequestMethod.GET)
    @Cacheable(value = PACKAGES_CACHE, keyGenerator = PACKAGE_KEY_GENERATOR)
    public Collection<PackageDTO> getPackages() throws Exception {
        final List<PackageEntity> packages = packageService.getAllPackages();
        return mapperStrategy.doMap(packages, PackageDTO.class);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @CacheEvict(value = PACKAGES_CACHE, allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    public void delete() throws Exception {
        packageService.deleteAllPackages();
    }

    @RequestMapping(value = "customers/{customer-uid}", method = RequestMethod.DELETE)
    @CacheEvict(value = PACKAGES_CACHE, keyGenerator = PACKAGE_KEY_GENERATOR)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("customer-uid") final String customerUid) {
        packageService.deletePackagesByCustomerUid(customerUid);
    }

    @RequestMapping(value = "/{package-uid}", method = RequestMethod.DELETE)
    @CacheEvict(value = PACKAGES_CACHE, keyGenerator = PACKAGE_KEY_GENERATOR)
    @ResponseStatus(HttpStatus.OK)
    public void deleteByPackageUid(@PathVariable("package-uid") final String packageUid) {
        final PackagePK pk = packagePKService.decode(packageUid);
        packageService.deleteByPk(pk);
    }

    private Function<PackageEntity, PackageDTO> getPackageMapper() {
        return packag -> {
            try {
                return mapperStrategy.doMap(packag, PackageDTO.class);
            } catch (Exception e) {
                throw new MappingException(e);
            }
        };
    }

}

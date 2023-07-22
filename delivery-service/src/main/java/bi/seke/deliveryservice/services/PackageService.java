package bi.seke.deliveryservice.services;

import bi.seke.deliveryservice.entities.PackageEntity;
import bi.seke.deliveryservice.entities.PackagePK;

import java.util.List;
import java.util.Optional;

public interface PackageService {
    /**
     * Finds all the packages which belongs to the client represented by {@code customerUid}
     *
     * @param customerUid
     * @return list of packages
     */
    List<PackageEntity> getPackagesByCustomerUid(String customerUid);

    /**
     * Gets all the packages in the service
     *
     * @return list of packages
     */
    List<PackageEntity> getAllPackages();

    /**
     * Deletes all the packages which belongs to the customer represented by {@code  customerUid}
     *
     * @param customerUid
     */
    void deletePackagesByCustomerUid(String customerUid);

    /**
     * Deletes all the packages in the service
     */
    void deleteAllPackages();

    /**
     * Gets the package by its pk
     *
     * @param packagePK
     * @return
     */

    Optional<PackageEntity> getPackageByUid(PackagePK packagePK);

    /**
     * Deletes a package by its pk
     *
     * @param pk
     */
    void deleteByPk(PackagePK pk);
}

package bi.seke.deliveryservice.mappers.impl;

import bi.seke.data.dtos.PackageDTO;
import bi.seke.deliveryservice.entities.PackageEntity;
import bi.seke.deliveryservice.mappers.DTOMapper;
import bi.seke.deliveryservice.services.PackagePKService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PackageEntryToDTOMapper implements DTOMapper<PackageEntity, PackageDTO> {
    protected final PackagePKService packagePKService;

    @Override
    public PackageDTO map(final PackageEntity source, final PackageDTO target) {
        final String packageUid = packagePKService.encode(source.getPk());
        target.setPackageUid(packageUid);
        return target;
    }

}

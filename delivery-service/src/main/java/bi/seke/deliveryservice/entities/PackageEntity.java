package bi.seke.deliveryservice.entities;

import java.util.UUID;


public class PackageEntity {
    private PackagePK pk;
    private UUID routeID;
    private double weight;
    private double volume;
    private boolean fragile;
    private String email;
    private String phone;
    private UUID deliveryType;
    private String description;
    private String createdBy;
}

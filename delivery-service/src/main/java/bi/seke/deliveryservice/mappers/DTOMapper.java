package bi.seke.deliveryservice.mappers;

public interface DTOMapper<SOURCE, TARGET> {
    TARGET map(SOURCE source, TARGET target);
}

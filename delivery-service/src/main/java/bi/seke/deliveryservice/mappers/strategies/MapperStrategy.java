package bi.seke.deliveryservice.mappers.strategies;

import java.util.Collection;

public interface MapperStrategy {
    <SOURCE, TARGET> TARGET doMap(SOURCE source, Class<TARGET> targetClass) throws Exception;

    <SOURCE, TARGET> Collection<TARGET> doMap(Collection<SOURCE> sources, Class<TARGET> targetClass) throws Exception;
}

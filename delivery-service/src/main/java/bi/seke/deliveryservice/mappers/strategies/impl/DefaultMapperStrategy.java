package bi.seke.deliveryservice.mappers.strategies.impl;

import bi.seke.deliveryservice.mappers.DTOMapper;
import bi.seke.deliveryservice.mappers.strategies.MapperStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Log4j2
public class DefaultMapperStrategy implements MapperStrategy {
    protected final Map<Class, DTOMapper> dtoMappers;

    @Override
    public <SOURCE, TARGET> TARGET doMap(final SOURCE source, final Class<TARGET> targetClass) throws Exception {
        TARGET target = targetClass.getDeclaredConstructor().newInstance();
        BeanUtils.copyProperties(source, target);
        Optional.ofNullable(dtoMappers.get(targetClass))
                .ifPresent(mapper -> mapper.map(source, target));
        return target;
    }

    @Override
    public <SOURCE, TARGET> Collection<TARGET> doMap(final Collection<SOURCE> sources, final Class<TARGET> targetClass) throws Exception {
        return sources.stream()
                .map(source -> doMapWithTry(source, targetClass))
                .collect(Collectors.toList());
    }

    private <SOURCE, TARGET> TARGET doMapWithTry(final SOURCE source, final Class<TARGET> targetClass) {
        try {
            return doMap(source, targetClass);
        } catch (Exception e) {
            log.error("Could not map {} to target class {}", source.getClass(), targetClass);
            throw new RuntimeException(e);
        }
    }
}

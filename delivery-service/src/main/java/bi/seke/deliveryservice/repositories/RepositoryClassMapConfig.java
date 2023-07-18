package bi.seke.deliveryservice.repositories;

import bi.seke.deliveryservice.entities.DeliveryTypeEntity;
import bi.seke.deliveryservice.entities.PackageEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Map;

@Configuration
public class RepositoryClassMapConfig {
    @Bean
    public Map<Class<?>, CassandraRepository> repositoryClassMap(
            final DeliveryTypeRepository deliveryTypeRepository,
            final PackageRepository packageRepository) {

        return Map.of(
                DeliveryTypeEntity.class, deliveryTypeRepository,
                PackageEntity.class, packageRepository);
    }
}

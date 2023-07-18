package bi.seke.deliveryservice.strategy.impl;

import bi.seke.deliveryservice.strategy.SampleDataSaveStrategy;
import lombok.AllArgsConstructor;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service("sampleDataSaveStrategy")
@AllArgsConstructor
public class DefaultSampleDataSaveStrategy implements SampleDataSaveStrategy {
    protected final Map<Class<?>, CassandraRepository> repositoryClassMap;

    @Override
    public <DATA> void save(DATA data) {
        Optional.ofNullable(repositoryClassMap.get(data.getClass()))
                .ifPresent(repository -> repository.save(data));
    }
}

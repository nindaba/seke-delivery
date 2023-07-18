package bi.seke.deliveryservice.properties;

import bi.seke.deliveryservice.entities.DeliveryTypeEntity;
import bi.seke.deliveryservice.entities.PackageEntity;
import bi.seke.deliveryservice.strategy.SampleDataSaveStrategy;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.List.of;

@Configuration
@ConfigurationProperties(prefix = "init")
@Data
public class SampleDataProperties {
    public boolean save;
    public List<DeliveryTypeEntity> deliveryTypes;
    public List<PackageEntity> packages;
    @NestedConfigurationProperty
    public PackageEntity samplePackage;
    /**
     * This is not a property
     */
    @Resource(name = "sampleDataSaveStrategy")
    protected SampleDataSaveStrategy sampleDataSaveStrategy;

    @Bean
    public void saveConfigs() {
        List<Runnable> configs = of(
                () -> deliveryTypes.forEach(sampleDataSaveStrategy::save),
                () -> packages.forEach(sampleDataSaveStrategy::save)
        );

        if (save) {
            configs.forEach(Runnable::run);
        }
    }
}

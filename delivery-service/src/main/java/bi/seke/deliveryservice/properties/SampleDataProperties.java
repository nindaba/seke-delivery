package bi.seke.deliveryservice.properties;

import bi.seke.deliveryservice.entities.DeliveryTypeEntity;
import bi.seke.deliveryservice.entities.PackageEntity;
import bi.seke.deliveryservice.strategy.SampleDataSaveStrategy;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.List.of;

@Configuration
@Data
@ConfigurationProperties(prefix = "init")
public class SampleDataProperties {
    @NestedConfigurationProperty
    public PackageEntity samplePackage;
    private boolean save;
    private String packageUid;
    private List<DeliveryTypeEntity> deliveryTypes;
    private List<PackageEntity> packages;

    @Bean
    public CommandLineRunner saveData(final SampleDataSaveStrategy sampleDataSaveStrategy) {
        return args -> {
            List<Runnable> configs = of(
                    () -> deliveryTypes.forEach(sampleDataSaveStrategy::save),
                    () -> packages.forEach(sampleDataSaveStrategy::save)
            );

            if (save) {
                configs.forEach(Runnable::run);
            }
        };
    }
}

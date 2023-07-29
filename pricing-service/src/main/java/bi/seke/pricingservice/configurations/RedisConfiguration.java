package bi.seke.pricingservice.configurations;

import bi.seke.data.dtos.PackageDTO;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

import static bi.seke.pricingservice.configurations.Configurations.COMMA_DELIMITER;


@Configuration
public class RedisConfiguration {
    @Bean
    public RedisTemplate<String, ? extends Serializable> packageRedisTemplate(final RedisConnectionFactory factory) {
        final RedisTemplate<String, ? extends Serializable> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean
    public KeyGenerator packageKeyGenerator() {
        return (target, method, params) -> params.length == 0 ? method.getName() : Arrays.stream(params)
                .map(param -> param instanceof PackageDTO packag ? packag.getPackageUid() : toString(params))
                .collect(Collectors.joining(COMMA_DELIMITER));
    }

    protected String toString(final Object[] params) {
        return Arrays.stream(params)
                .map(Object::toString)
                .collect(Collectors.joining(COMMA_DELIMITER));
    }
}

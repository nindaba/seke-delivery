package bi.seke.pricingservice.entities;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Data
@Table
public class DefinitePriceEntity implements Serializable {
    @PrimaryKey
    private PricePK pk;
    private Double amount;
}

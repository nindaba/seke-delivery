package bi.seke.paymentservice.repositories;

import bi.seke.paymentservice.documents.PriceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface PriceRepository extends MongoRepository<PriceDocument, UUID> {
}

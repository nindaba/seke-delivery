package bi.seke.paymentservice.repositories;

import bi.seke.paymentservice.documents.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends MongoRepository<CustomerDocument, UUID> {
    Optional<CustomerDocument> findByCustomerUid(String customerUid);
}

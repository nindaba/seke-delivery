package bi.seke.paymentservice.repositories;

import bi.seke.paymentservice.documents.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<CustomerDocument, String> {
    Optional<CustomerDocument> findByCustomerUid(String customerUid);
}

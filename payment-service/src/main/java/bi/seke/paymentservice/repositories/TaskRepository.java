package bi.seke.paymentservice.repositories;

import bi.seke.paymentservice.documents.TaskDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends MongoRepository<TaskDocument, UUID> {
    Optional<TaskDocument> findByPackageUid(String packageUid);
}

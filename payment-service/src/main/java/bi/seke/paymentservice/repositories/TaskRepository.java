package bi.seke.paymentservice.repositories;

import bi.seke.paymentservice.documents.TaskDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TaskRepository extends MongoRepository<TaskDocument, String> {
    Optional<TaskDocument> findByPackageUid(String packageUid);
}

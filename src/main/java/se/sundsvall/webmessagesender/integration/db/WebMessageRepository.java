package se.sundsvall.webmessagesender.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import se.sundsvall.webmessagesender.integration.db.model.WebMessageEntity;

import java.util.List;

@Transactional
@CircuitBreaker(name = "WebMessageRepository")
public interface WebMessageRepository extends CrudRepository<WebMessageEntity, String> {

	List<WebMessageEntity> findByPartyIdOrderByCreated(String partyId);

	List<WebMessageEntity> findByExternalReferencesKeyAndExternalReferencesValueOrderByCreated(String key, String value);
}

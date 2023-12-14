package se.sundsvall.webmessagesender.integration.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import se.sundsvall.webmessagesender.integration.db.model.WebMessageEntity;

@Transactional
@CircuitBreaker(name = "webMessageRepository")
public interface WebMessageRepository extends CrudRepository<WebMessageEntity, String> {

	List<WebMessageEntity> findByPartyIdOrderByCreated(String partyId);

	List<WebMessageEntity> findByExternalReferencesKeyAndExternalReferencesValueOrderByCreated(String key, String value);
}

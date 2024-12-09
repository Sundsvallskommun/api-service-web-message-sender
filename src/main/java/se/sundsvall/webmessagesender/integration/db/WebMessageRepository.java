package se.sundsvall.webmessagesender.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import se.sundsvall.webmessagesender.integration.db.model.WebMessageEntity;

@Transactional
@CircuitBreaker(name = "webMessageRepository")
public interface WebMessageRepository extends CrudRepository<WebMessageEntity, String> {

	List<WebMessageEntity> findByMunicipalityIdAndPartyIdOrderByCreated(String municipalityId, String partyId);

	Optional<WebMessageEntity> findByMunicipalityIdAndId(String municipalityId, String id);

	List<WebMessageEntity> findByMunicipalityIdAndExternalReferencesKeyAndExternalReferencesValueOrderByCreated(String municipalityId, String key, String value);
}

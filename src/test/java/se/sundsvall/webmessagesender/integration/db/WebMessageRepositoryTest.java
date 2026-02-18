package se.sundsvall.webmessagesender.integration.db;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.webmessagesender.integration.db.model.AttachmentEntity;
import se.sundsvall.webmessagesender.integration.db.model.ExternalReferenceEntity;
import se.sundsvall.webmessagesender.integration.db.model.WebMessageEntity;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * WebMessage repository tests.
 *
 * @see src/test/resources/db/scripts/WebMessageRepositoryTest.sql for data setup.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
@Sql(scripts = {
	"/db/scripts/truncate.sql",
	"/db/scripts/test-unit.sql"
})
class WebMessageRepositoryTest {

	private static final String MUNICIPALITY_ID = "2281";
	private static final String ENTITY_1_ID = "1e098e28-d9ba-459c-94c7-5508be826c08";
	private static final String ENTITY_1_PARTY_ID = "fbfbd90c-4c47-11ec-81d3-0242ac130003";
	private static final String ENTITY_2_ID = "68cd9896-9918-4a80-bb41-e8fe0faf03f9";
	private static final String ENTITY_3_ID = "3472ab5b-fca7-4eaf-8eec-8115c9710526";
	private static final String ENTITY_4_ID = "e535c9f7-c473-44f2-81d5-a8fbfcc932ea";
	private static final String ENTITY_5_ID = "3472ab5b-fca7-4eaf-8eec-8115c9710527";

	@Autowired
	private WebMessageRepository webMessageRepository;

	@Test
	void findByMunicipalityIdAndId() {

		// Call
		final var webMessageOptional = webMessageRepository.findByMunicipalityIdAndId(MUNICIPALITY_ID, ENTITY_1_ID);

		// Verification
		assertThat(webMessageOptional).isPresent();
		assertThat(webMessageOptional.get().getId()).isEqualTo(ENTITY_1_ID);
		assertThat(webMessageOptional.get().getMunicipalityId()).isEqualTo(MUNICIPALITY_ID);
		assertThat(webMessageOptional.get().getPartyId()).isEqualTo(ENTITY_1_PARTY_ID);
		assertThat(webMessageOptional.get().getExternalReferences()).hasSize(1);
	}

	@Test
	void findByMunicipalityIdAndIdNotFound() {

		// Call
		final var webMessageOptional = webMessageRepository.findByMunicipalityIdAndId(MUNICIPALITY_ID, "does-not-exist");

		// Verification
		assertThat(webMessageOptional).isNotPresent();
	}

	@Test
	void findByMunicipalityIdAndPartyId() {

		// Call
		final var webMessageList = webMessageRepository.findByMunicipalityIdAndPartyIdOrderByCreated(MUNICIPALITY_ID, ENTITY_1_PARTY_ID);

		// Verification
		assertThat(webMessageList).isNotNull().hasSize(1);
		assertThat(webMessageList.getFirst().getId()).isEqualTo(ENTITY_1_ID);
		assertThat(webMessageList.getFirst().getMunicipalityId()).isEqualTo(MUNICIPALITY_ID);
		assertThat(webMessageList.getFirst().getPartyId()).isEqualTo(ENTITY_1_PARTY_ID);
		assertThat(webMessageList.getFirst().getExternalReferences()).hasSize(1);
	}

	@Test
	void findByMunicipalityIdAndPartyIdNotFound() {

		// Call
		final var webMessageList = webMessageRepository.findByMunicipalityIdAndPartyIdOrderByCreated(MUNICIPALITY_ID, "does-not-exist");

		// Verification
		assertThat(webMessageList).isEmpty();
	}

	@Test
	void findByMunicipalityIdAndExternalReferencesKeyValue() {

		// Call
		final var webMessagesList = webMessageRepository.findByMunicipalityIdAndExternalReferencesKeyAndExternalReferencesValueOrderByCreated(MUNICIPALITY_ID, "common-key", "common-value");

		// Verification
		assertThat(webMessagesList).hasSize(3);
		assertThat(webMessagesList).extracting(WebMessageEntity::getId).containsExactlyInAnyOrder(ENTITY_2_ID, ENTITY_3_ID, ENTITY_5_ID);
	}

	@Test
	void findByMunicipalityIdAndExternalReferencesKeyValueNotFound() {

		// Call
		final var webMessagesList = webMessageRepository.findByMunicipalityIdAndExternalReferencesKeyAndExternalReferencesValueOrderByCreated(MUNICIPALITY_ID, "does-not-exist", "does-not-exist");

		// Verification
		assertThat(webMessagesList).isNotNull().isEmpty();
	}

	@Test
	void persist() {

		// Setup
		final var webMessageEntity = WebMessageEntity.create()
			.withExternalReferences(List.of(ExternalReferenceEntity.create().withKey("key").withValue("value")))
			.withMessage("Some message")
			.withPartyId(UUID.randomUUID().toString())
			.withAttachments(List.of(AttachmentEntity.create().withFile(UUID.randomUUID().toString().getBytes())));

		// Call
		final var persistedEntity = webMessageRepository.save(webMessageEntity);

		// Verification
		assertThat(persistedEntity).isEqualTo(webMessageEntity);
		assertThat(persistedEntity.getCreated()).isCloseTo(OffsetDateTime.now(), within(2, SECONDS));
		assertThat(isValidUUID(persistedEntity.getId())).isTrue();
	}

	@Test
	void persistUniqueConstraintValidationFailes() {

		// Setup
		final var webMessageEntity = WebMessageEntity.create()
			.withExternalReferences(List.of(
				ExternalReferenceEntity.create().withKey("key1").withValue("value1"),
				ExternalReferenceEntity.create().withKey("key1").withValue("value1")))
			.withMessage("Some message")
			.withPartyId(UUID.randomUUID().toString());

		// Call
		final var exception = assertThrows(DataIntegrityViolationException.class, () -> webMessageRepository.save(webMessageEntity));

		// Verification
		assertThat(exception.getMostSpecificCause().getMessage()).contains("Duplicate entry", "key1", "value1", "for key \'unique_external_reference\'");
	}

	@Test
	void delete() {

		// Pre-verification
		final var entity = webMessageRepository.findById(ENTITY_4_ID);
		assertThat(entity).isPresent();

		// Call
		webMessageRepository.delete(entity.get());

		// Post-verification
		assertThat(webMessageRepository.findById(ENTITY_4_ID)).isNotPresent();
	}

	private boolean isValidUUID(String value) {
		try {
			UUID.fromString(String.valueOf(value));
		} catch (final Exception e) {
			return false;
		}

		return true;
	}
}

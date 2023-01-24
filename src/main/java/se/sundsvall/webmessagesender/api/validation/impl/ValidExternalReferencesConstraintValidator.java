package se.sundsvall.webmessagesender.api.validation.impl;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.springframework.util.CollectionUtils.isEmpty;
import org.apache.commons.lang3.StringUtils;
import static se.sundsvall.webmessagesender.service.ServiceConstants.REFERENCE_FLOW_INSTANCE_ID;

import java.util.List;
import java.util.function.Predicate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import se.sundsvall.webmessagesender.api.model.ExternalReference;
import se.sundsvall.webmessagesender.api.validation.ValidExternalReferences;

public class ValidExternalReferencesConstraintValidator implements ConstraintValidator<ValidExternalReferences, List<ExternalReference>> {
	private static final String CUSTOM_ERROR_MESSAGE = String.format("element with key '%s' must have value of numeric type", REFERENCE_FLOW_INSTANCE_ID);

	@Override
	public boolean isValid(final List<ExternalReference> externalReferences, final ConstraintValidatorContext context) {
		if (isEmpty(externalReferences)) {
			return false;
		}

		return externalReferences.stream().allMatch(isValidExternalReference(context));
	}

	private Predicate<ExternalReference> isValidExternalReference(ConstraintValidatorContext context) {
		return externalReference -> {
			boolean noneBlank = isNoneBlank(externalReference.getKey(), externalReference.getValue());
			if (noneBlank) {
				boolean validValue = validValue(externalReference);
				if (!validValue) {
					useCustomMessageForValidation(context, CUSTOM_ERROR_MESSAGE);
				}
				
				return validValue;
			}
			
			return noneBlank;
		};
	}
	
	/**
	 * Method to verify value of object. Returns true if key is not equal to flowInstanceId 
	 * (case insensitive) or if value is numeric (which only is verified if key is equal to 
	 * flowinstanceId).
	 * 
	 * @param externalReference reference to check
	 * @return true if valid, false otherwise
	 */
	private boolean validValue(ExternalReference externalReference) {
		return !REFERENCE_FLOW_INSTANCE_ID.equalsIgnoreCase(externalReference.getKey()) ||
				StringUtils.isNumeric(externalReference.getValue());
	}
	
	private void useCustomMessageForValidation(ConstraintValidatorContext constraintContext, String message) {
		constraintContext.disableDefaultConstraintViolation();
		constraintContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}

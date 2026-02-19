package se.sundsvall.webmessagesender.api.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import se.sundsvall.webmessagesender.api.validation.ValidInstance;

import static java.util.Objects.isNull;

public class ValidInstanceConstraintValidator implements ConstraintValidator<ValidInstance, String> {

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		if (isNull(value)) {
			return false;
		}
		return value.equalsIgnoreCase("internal") || value.equalsIgnoreCase("external");
	}

}

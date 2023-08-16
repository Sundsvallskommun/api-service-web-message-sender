package se.sundsvall.webmessagesender.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import se.sundsvall.webmessagesender.api.validation.impl.ValidExternalReferencesConstraintValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidExternalReferencesConstraintValidator.class)
public @interface ValidExternalReferences {

	String message() default "can not be empty or contain elements with empty keys or values";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

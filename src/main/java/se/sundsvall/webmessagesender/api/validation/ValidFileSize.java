package se.sundsvall.webmessagesender.api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import se.sundsvall.webmessagesender.api.validation.impl.ValidFileSizeConstraintValidator;

@Documented
@Target({ ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidFileSizeConstraintValidator.class)
public @interface ValidFileSize {
	String message() default "object exceeds the maximum allowed size";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

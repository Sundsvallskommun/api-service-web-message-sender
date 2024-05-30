package se.sundsvall.webmessagesender.api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import se.sundsvall.webmessagesender.api.validation.impl.ValidInstanceConstraintValidator;

@Documented
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidInstanceConstraintValidator.class)
public @interface ValidInstance {

	String message() default "instance can only be 'internal' or 'external'";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

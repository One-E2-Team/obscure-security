package rs.ac.uns.ftn.e2.onee2team.security.pki.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
	 String message() default "Password is too weak. Please choose another password.";
	 Class<?>[] groups() default {};
	 Class<? extends Payload>[] payload() default {};
}

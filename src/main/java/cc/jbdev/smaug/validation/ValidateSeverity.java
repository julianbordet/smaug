package cc.jbdev.smaug.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CheckSeverityValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateSeverity {

    public String value();

    public String message() default "Not valid";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}

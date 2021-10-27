package cc.jbdev.smaug.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckPriorityValidator implements ConstraintValidator<ValidatePriority, String> {

    @Override
    public void initialize(ValidatePriority constraintAnnotation) {

    }

    @Override
    public boolean isValid(String stringProvidedByUser, ConstraintValidatorContext constraintValidatorContext) {

        boolean result;

        if(stringProvidedByUser != null && (stringProvidedByUser.equals("HIGH") || stringProvidedByUser.equals("MEDIUM") || stringProvidedByUser.equals("LOW"))){
            result = true;
        } else {
            result = false;
        }

        return result;
    }
}

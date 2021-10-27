package cc.jbdev.smaug.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckSeverityValidator implements ConstraintValidator<ValidateSeverity, String> {



    @Override
    public void initialize(ValidateSeverity valueToCheckAgainst) {

    }

    @Override
    public boolean isValid(String stringProvidedByUser, ConstraintValidatorContext constraintValidatorContext) {

        boolean result;

        if(stringProvidedByUser != null && (stringProvidedByUser.equals("CRITICAL") || stringProvidedByUser.equals("MAJOR") || stringProvidedByUser.equals("MINOR") || stringProvidedByUser.equals("LOW"))){
            result = true;
        } else {
            result = false;
        }

        return result;
    }
}

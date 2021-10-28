package cc.jbdev.smaug.validation;

import cc.jbdev.smaug.entity.Developer;
import cc.jbdev.smaug.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class CheckAssignedDeveloper implements ConstraintValidator<ValidateAssignedDeveloper, String> {

    @Autowired
    ProjectService projectService;

    @Override
    public void initialize(ValidateAssignedDeveloper constraintAnnotation) {

    }

    @Override
    public boolean isValid(String stringProvidedByUser, ConstraintValidatorContext constraintValidatorContext) {

        boolean result = false;

        List<String> developerList =  projectService.getListOfActiveDevelopers();

        if(stringProvidedByUser != null){

            for(String validDeveloperUsername : developerList){

                if(stringProvidedByUser.equals(validDeveloperUsername)){
                    result = true;
                    break;
                }

            }

        } else {
            result = false;
        }

        return result;
    }
}

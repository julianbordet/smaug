package cc.jbdev.smaug.validation;

import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class CheckAssignedProject implements ConstraintValidator<ValidateAssignedProject, Integer> {

    @Autowired
    ProjectService projectService;

    @Override
    public void initialize(ValidateAssignedProject constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer projectIdProvidedByUser, ConstraintValidatorContext constraintValidatorContext) {

        boolean result = false;

        List<Project> projectList =  projectService.getActiveProjects();
        List<Integer> projectIds = new ArrayList<>();

        for(Project project : projectList){
            projectIds.add(project.getProjectId());
        }

        try {

            for(Integer validProjectId : projectIds){
                if(projectIdProvidedByUser == validProjectId){
                    result = true;
                    break;
                }
            }

        } catch (Exception e) {
            result = false;
        }



        return result;

    }
}

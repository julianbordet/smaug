package cc.jbdev.smaug.validation;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidationUtil {
    /*
    List<String> devList = new ArrayList<>();
    String developerSelectedByUser;

    public ValidationUtil(List<String> developerList, String developerSelectedByUser) {
        this.devList = developerList;
        this.developerSelectedByUser = developerSelectedByUser;
    }*/

    public boolean validateDeveloper(List<String> developerList, String developerSelectedByUser){

        boolean result = false;

        for(String developer : developerList){
            if(developer.equals(developerSelectedByUser)){
                result = true;
                break;
            }
        }

        return result;
    }

    public boolean validateProject(List<Project> projectList, int projectSelectedByUser){

        boolean result = false;
        List<Integer> projectIdList = new ArrayList<>();

        for(Project project : projectList){
            projectIdList.add(project.getProjectId());
        }

        for(Integer validProjectId : projectIdList){

            if(validProjectId == projectSelectedByUser){
                result = true;
                break;
            }

        }

        return result;
    }

    public boolean validateDueDate(String dateProvidedByUser){

        boolean result = false;

        //validates a date based on gregorian calendar
        Pattern pattern = Pattern.compile("^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
                + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
                + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
                + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");
        Matcher matcher = pattern.matcher(dateProvidedByUser);
        result = matcher.matches();

        return result;
    }

    public boolean validatePriority(String priorityProvidedByUser){

        boolean result = false;

        if(priorityProvidedByUser != null && (priorityProvidedByUser.equals("HIGH") || priorityProvidedByUser.equals("MEDIUM") || priorityProvidedByUser.equals("LOW"))){
            result = true;
        }

        return result;
    }

    public boolean validateSeverity(String severityProvidedByUser){

        boolean result = false;

        if(severityProvidedByUser != null && (severityProvidedByUser.equals("CRITICAL") || severityProvidedByUser.equals("MAJOR") || severityProvidedByUser.equals("MINOR") || severityProvidedByUser.equals("LOW"))){
            result = true;
        }

        return result;

    }

    public boolean validateTitle(String titleProvidedByUser){

        boolean result = false;

        if(titleProvidedByUser.length() > 0 && titleProvidedByUser.length() <= 50){
            result = true;
        }

        return result;
    }

    public boolean validateNull(Object obj){

        boolean result = Objects.nonNull(obj);

        return result;
    }

    public boolean validateNewBug(Bug theBug, ProjectService projectService){

        boolean result = false;

        if(
                validateProject(projectService.getActiveProjects(), theBug.getProjectId()) &&
                validateDeveloper(projectService.getListOfActiveDevelopers(), theBug.getBugResponsibleDev()) &&
                validateDueDate(theBug.getBugDueDate()) &&
                validatePriority(theBug.getBugPriority()) &&
                validateSeverity(theBug.getBugSeverity()) &&
                validateTitle(theBug.getBugTitle()) &&
                validateNull(theBug)
        ){
            result = true;
        }

        return result;
    }

}
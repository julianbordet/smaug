package cc.jbdev.smaug.utility;

import cc.jbdev.smaug.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceUtils {
    private static ServiceUtils instance;

    @Autowired
    private ProjectService projectService;

    /* Post constructor */

    @PostConstruct
    public void fillInstance() {
        instance = this;
    }

    /*static methods */

    public static ProjectService getProjectService() {
        return instance.projectService;
    }




}
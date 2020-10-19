package cc.jbdev.smaug.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="users")
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="enabled")
    private int enabled;


    //Setting up JoinTable for project_developer. This table keeps track
    //of which developers participate in which projects, and vice versa.
    @ManyToMany
    @JoinTable(
            name="project_developer",
            joinColumns = @JoinColumn(name="dev_username"),
            inverseJoinColumns = @JoinColumn(name="project_id")
    )
    private List<Project> projects;


    public Developer() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}

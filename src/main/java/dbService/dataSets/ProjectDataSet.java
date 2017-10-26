package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "projects")
public class ProjectDataSet implements Serializable {
    private static final long serialVersionUID = 26102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "project_name", unique = true)
    private String projectName;

    @OneToMany(mappedBy = "project")
    private Set<RequestDataSet> requests;

    @OneToMany(mappedBy = "project")
    private Set<ProjectPositionDataSet> projectPositionDataSets;


    public ProjectDataSet() {
    }

    public ProjectDataSet(String projectName) {
        this.projectName = projectName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "ProjectDataSet{" +
                "id=" + id +
                ", project_name='" + projectName + '\'' +
                '}';
    }
}

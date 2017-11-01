package dbService.dataSets;

import javax.persistence.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "projects")
public class ProjectDataSet implements Serializable {
    private static final long serialVersionUID = 1_11_2017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "projects_id_generator")
    @SequenceGenerator(name = "projects_id_generator", sequenceName = "projects_id_seq")
    private long id;

    @Column(name = "title", unique = true)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserDataSet creator;

    @OneToMany(mappedBy = "project")
    private Set<RequestDataSet> requests;

    @OneToMany(mappedBy = "project")
    private Set<ProjectPositionDataSet> projectPositions;


    public ProjectDataSet() {
    }

    public ProjectDataSet(String title, String description, UserDataSet creator) {
        this.title = title;
        this.description = description;
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreator(UserDataSet creator) {
        this.creator = creator;
    }

    public UserDataSet getCreator() {
        return creator;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String projectName) {
        this.title = projectName;
    }

    @Override
    public String toString() {
        return "ProjectDataSet{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + title + '\'' +
                ", creator_id='" + creator.getId() + '\'' +
                '}';
    }
}

package dbService.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "projects")
public class ProjectEntity implements Serializable {
    private static final long serialVersionUID = 2_11_2017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "description", columnDefinition="text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @OneToMany(mappedBy = "project")
    private Set<RequestEntity> requests;

    @OneToMany(mappedBy = "project")
    private Set<ProjectPositionEntity> projectPositions;


    public ProjectEntity() {
    }

    public ProjectEntity(String title, String description, UserEntity creator) {
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

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public UserEntity getCreator() {
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
        return "ProjectEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + title + '\'' +
                ", creator_id='" + creator.getId() + '\'' +
                '}';
    }
}

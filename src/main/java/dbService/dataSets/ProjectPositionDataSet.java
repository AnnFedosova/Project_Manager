package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "project_positions")
public class ProjectPositionDataSet implements Serializable{
    private static final long serialVersionUID = 26102017L;

    @Id
    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectDataSet project;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDataSet user;

    @Id
    @ManyToOne
    @JoinColumn(name = "position_id")
    private PositionDataSet position;

    public ProjectPositionDataSet() {

    }
}

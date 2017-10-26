package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "request_positions", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "request_id", "position_id"}))
public class RequestPositionDataSet  implements Serializable {
    private static final long serialVersionUID = 26102017L;

    @Id
    @ManyToOne
    @JoinColumn(name = "request_id")
    private RequestDataSet request;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDataSet user;

    @Id
    @ManyToOne
    @JoinColumn(name = "position_id")
    private PositionDataSet position;

    public RequestPositionDataSet() {

    }

    public UserDataSet getUser() {
        return user;
    }

    public void setUser(UserDataSet user) {
        this.user = user;
    }

    public PositionDataSet getPosition() {
        return position;
    }

    public void setPosition(PositionDataSet position) {
        this.position = position;
    }

    public RequestDataSet getRequest() {
        return request;
    }

    public void setRequest(RequestDataSet request) {
        this.request = request;
    }
}



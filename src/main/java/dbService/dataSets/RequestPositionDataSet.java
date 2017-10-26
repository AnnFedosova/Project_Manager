package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "request_positions")
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
}



package dbService.dao;

import dbService.dataSets.PositionDataSet;
import dbService.dataSets.ProjectDataSet;
import dbService.dataSets.ProjectPositionDataSet;
import dbService.dataSets.UserDataSet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Evgeny Levin
 */
public class ProjectPositionDAO {
    private final Session session;

    public ProjectPositionDAO(Session session) {
        this.session = session;
    }

    public ProjectPositionDataSet get(long id)  throws HibernateException {
        return session.get(ProjectPositionDataSet.class, id);
    }

    public List<ProjectPositionDataSet> get(UserDataSet user)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProjectPositionDataSet> criteria = builder.createQuery(ProjectPositionDataSet.class);
        Root<ProjectPositionDataSet> root = criteria.from(ProjectPositionDataSet.class);
        ParameterExpression<UserDataSet> parameter = builder.parameter(UserDataSet.class);
        criteria.select(root).where(builder.equal(root.get("user"), parameter));
        Query<ProjectPositionDataSet> query = session.createQuery(criteria);
        query.setParameter(parameter, user);
        return query.getResultList();
    }

    public List<ProjectPositionDataSet> get(String login)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProjectPositionDataSet> criteria = builder.createQuery(ProjectPositionDataSet.class);
        Root<ProjectPositionDataSet> root = criteria.from(ProjectPositionDataSet.class);
        ParameterExpression<UserDataSet> parameter = builder.parameter(UserDataSet.class);
        criteria.select(root).where(builder.equal(root.get("user"), parameter));
        Query<ProjectPositionDataSet> query = session.createQuery(criteria);
        UserDAO userDAO = new UserDAO(session);
        query.setParameter(parameter, userDAO.get(login));
        return query.getResultList();
    }

    public void addProjectPosition(long projectId, String positionName, String userLogin) {
        PositionDAO positionDAO = new PositionDAO(session);
        ProjectDAO projectDAO = new ProjectDAO(session);
        UserDAO userDAO = new UserDAO(session);

        PositionDataSet position = positionDAO.get(positionName);
        ProjectDataSet project = projectDAO.get(projectId);
        UserDataSet user = userDAO.get(userLogin);

        session.save(new ProjectPositionDataSet(project, position, user));
    }

    public void addProjectPosition(PositionDataSet position, ProjectDataSet project, UserDataSet user) {
        session.save(new ProjectPositionDataSet(project, position, user));
    }

    public void addProjectPosition(ProjectPositionDataSet projectPosition) {
        session.save(projectPosition);
    }
}

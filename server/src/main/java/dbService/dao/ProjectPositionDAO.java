package dbService.dao;

import dbService.entities.PositionEntity;
import dbService.entities.ProjectEntity;
import dbService.entities.ProjectPositionEntity;
import dbService.entities.UserEntity;
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

    public ProjectPositionEntity get(long id)  throws HibernateException {
        return session.get(ProjectPositionEntity.class, id);
    }

    public List<ProjectPositionEntity> getByUser(UserEntity user)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProjectPositionEntity> criteria = builder.createQuery(ProjectPositionEntity.class);
        Root<ProjectPositionEntity> root = criteria.from(ProjectPositionEntity.class);
        ParameterExpression<UserEntity> parameter = builder.parameter(UserEntity.class);
        criteria.select(root).where(builder.equal(root.get("user"), parameter));
        Query<ProjectPositionEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, user);
        return query.getResultList();
    }

    public List<ProjectPositionEntity> getByUser(String login)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProjectPositionEntity> criteria = builder.createQuery(ProjectPositionEntity.class);
        Root<ProjectPositionEntity> root = criteria.from(ProjectPositionEntity.class);
        ParameterExpression<UserEntity> parameter = builder.parameter(UserEntity.class);
        criteria.select(root).where(builder.equal(root.get("user"), parameter));
        Query<ProjectPositionEntity> query = session.createQuery(criteria);
        UserDAO userDAO = new UserDAO(session);
        query.setParameter(parameter, userDAO.get(login));
        return query.getResultList();
    }

    public List<ProjectPositionEntity> getByProjectId(long projectId)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProjectPositionEntity> criteria = builder.createQuery(ProjectPositionEntity.class);
        Root<ProjectPositionEntity> root = criteria.from(ProjectPositionEntity.class);
        ParameterExpression<ProjectEntity> parameter = builder.parameter(ProjectEntity.class);
        criteria.select(root).where(builder.equal(root.get("project"), parameter));
        Query<ProjectPositionEntity> query = session.createQuery(criteria);
        ProjectDAO projectDAO = new ProjectDAO(session);
        query.setParameter(parameter, projectDAO.get(projectId));
        return query.getResultList();
    }

    public void addProjectPosition(long projectId, String positionName, String userLogin) {
        PositionDAO positionDAO = new PositionDAO(session);
        ProjectDAO projectDAO = new ProjectDAO(session);
        UserDAO userDAO = new UserDAO(session);

        PositionEntity position = positionDAO.get(positionName);
        ProjectEntity project = projectDAO.get(projectId);
        UserEntity user = userDAO.get(userLogin);

        session.save(new ProjectPositionEntity(project, position, user));
    }

    public void addProjectPosition(PositionEntity position, ProjectEntity project, UserEntity user) {
        session.save(new ProjectPositionEntity(project, position, user));
    }

    public void addProjectPosition(ProjectPositionEntity projectPosition) {
        session.save(projectPosition);
    }
}

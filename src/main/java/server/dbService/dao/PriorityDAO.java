package server.dbService.dao;

import server.dbService.entities.PriorityEntity;
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
public class PriorityDAO {
    private final Session session;

    public PriorityDAO(Session session) {
        this.session = session;
    }

    public PriorityEntity get(long id)  throws HibernateException {
        return session.get(PriorityEntity.class, id);
    }

    public PriorityEntity get(String priorityName)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PriorityEntity> criteria = builder.createQuery(PriorityEntity.class);

        Root<PriorityEntity> root = criteria.from(PriorityEntity.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("name"), parameter));
        Query<PriorityEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, priorityName);
        return query.uniqueResult();
    }

    public long addPriority(String name) {
        return (long) session.save(new PriorityEntity(name));
    }

    public long addPriority(PriorityEntity priority) {
        return (long) session.save(priority);
    }

    public List<PriorityEntity> selectAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PriorityEntity> criteria = builder.createQuery(PriorityEntity.class);
        Root<PriorityEntity> root = criteria.from(PriorityEntity.class);
        criteria.select(root);
        Query<PriorityEntity> query = session.createQuery(criteria);
        return query.getResultList();
    }
}

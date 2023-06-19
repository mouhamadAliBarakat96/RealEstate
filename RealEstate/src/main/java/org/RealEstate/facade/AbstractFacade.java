package org.RealEstate.facade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.interfaces.ICRUDOperations;
import org.RealEstate.utils.Utils;
import org.eclipse.persistence.internal.jpa.EJBQueryImpl;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;
import org.eclipse.persistence.sessions.Session;

@Stateless
public abstract class AbstractFacade<T> implements Serializable, ICRUDOperations<T> {

	private static final long serialVersionUID = -1L;
	protected Class<T> entityClass;
	@PersistenceContext(unitName = "realEstate-persistence-unit")
	public EntityManager em;

	public AbstractFacade(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected EntityManager getEntityManager() {
		return em;
	}

	public T save(T entity) throws Exception {
		return getEntityManager().merge(entity);
	}

	public void remove(T entity) throws Exception {
		getEntityManager().remove(getEntityManager().merge(entity));
	}

	public void remove(T[] entityArray) throws Exception {
		for (T entity : entityArray) {
			getEntityManager().remove(getEntityManager().merge(entity));
		}
	}

	public T findWithLockPessimisticWriteWithoutException(Object id) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("jakarta.persistence.lock.timeout", 6500L);
		return getEntityManager().find(entityClass, id, LockModeType.PESSIMISTIC_WRITE, properties);
	}

	public T find(Object id) {
		return getEntityManager().find(entityClass, id);
	}
	/*
	 * public T findWithoutExecption() { // try { // ge // } }
	 */

	public List<T> findAll() {
		CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));
		return getEntityManager().createQuery(cq).getResultList();
	}

	public Response findAllForApi() {
		try {

			return Response.status(Status.OK).entity(Utils.listToString(this.findAll())).build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}

	public List<T> findRange(int[] range) {
		CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));

		return getEntityManager().createQuery(cq).setMaxResults(range[1] - range[0]).setFirstResult(range[0])
				.getResultList();
	}

	public List<T> findPagintion(int size, int offset) {
		CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));

		return getEntityManager().createQuery(cq).setMaxResults(size).setFirstResult(offset).getResultList();
	}

	public int count() {
		CriteriaQuery<Long> cq = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
		Root<T> root = cq.from(entityClass);
		cq.select(getEntityManager().getCriteriaBuilder().count(root));
		return (getEntityManager().createQuery(cq).getSingleResult()).intValue();
	}

	public T newEntity() throws InstantiationException, IllegalAccessException {
		return this.entityClass.newInstance();
	}

	public String getSqlFromJpql(Query query) {
		try {
			Session s = em.unwrap(JpaEntityManager.class).getActiveSession();
			DatabaseQuery dq = ((EJBQueryImpl<?>) query).getDatabaseQuery();
			dq.prepareCall(s, new DatabaseRecord());
			System.out.println(dq.getSQLString());
			return dq.getSQLString();
		} catch (Exception e) {
			System.out.println("error in query");
		}
		return null;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	
	public List<T> saveAll(List<T> list) throws Exception{

		for (int i = 0; i < list.size(); i++) {
			list.set(i, save(list.get(i)));
		}

		return list;
	}

}
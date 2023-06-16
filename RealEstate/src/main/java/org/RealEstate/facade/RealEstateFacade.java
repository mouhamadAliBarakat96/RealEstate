package org.RealEstate.facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.RealEstate.enumerator.PostType;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.RealEstate.utils.Constants;
import org.eclipse.persistence.config.QueryHints;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
public class RealEstateFacade extends AbstractFacade<RealEstate> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RealEstateFacade() {
		super(RealEstate.class);
	}

	public Long findUserCountPost(Long userId) {
		return (Long) getEntityManager().createNamedQuery(RealEstate.FING_NB_POST_FOR_USER)
				.setParameter("userId", userId).getSingleResult();
	}

	public List<RealEstate> findAllRealSatateWithFilter(User user, String postType, int minPrice, int maxPrice,
			Village village, int page, int size  , AtomicLong totalCount ) throws Exception {
		List<RealEstate> realEstates;

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<RealEstate> criteriaQuery = criteriaBuilder.createQuery(RealEstate.class);
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<RealEstate> root = countQuery.from(RealEstate.class);

		// Create a list of predicates based on your runtime conditions
		List<Predicate> predicates = new ArrayList<>();

		if (user != null) {
			predicates.add(criteriaBuilder.equal(root.get("user"), user));

		}
		if (postType != null) {
			PostType postTypeEnum = PostType.findEnum(postType);
			if (postTypeEnum == null) {
				throw new Exception(Constants.POST_TYPE_NOT_SUPPORTED);
			}
			predicates.add(criteriaBuilder.equal(root.get("postType"), postTypeEnum));

		}

		if (minPrice > 0) {

			Predicate minPricePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
			predicates.add(minPricePredicate);
		}
		if (maxPrice > 0) {
			Predicate maxPricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
			predicates.add(maxPricePredicate);
		}

		if (village != null) {
			predicates.add(criteriaBuilder.equal(root.get("village"), village));

		}

		// Combine the predicates using conjunction (AND) or disjunction (OR)
		Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));

		criteriaQuery.where(finalPredicate);

		countQuery.select(criteriaBuilder.count(root)).where(finalPredicate);

		totalCount.set(getEntityManager().createQuery(countQuery).getSingleResult()); ;
		 
		criteriaQuery.select(root).where(finalPredicate);
		TypedQuery<RealEstate> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setHint("eclipselink.join-fetch", "RealEstate.village")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district.governorate")
				.setHint("eclipselink.join-fetch", "RealEstate.user");
		typedQuery.setFirstResult((page - 1) * size);
		typedQuery.setMaxResults(page);

		// Add predicates based on your conditions
		realEstates = typedQuery

				.getResultList();
		return realEstates;

	}

}

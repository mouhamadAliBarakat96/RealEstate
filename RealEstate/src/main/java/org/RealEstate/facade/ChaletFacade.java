package org.RealEstate.facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.enumerator.PostType;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.RealEstate.service.UploadImagesMultiPart;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

@Stateless

public class ChaletFacade extends AbstractFacade<Chalet> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;

	public ChaletFacade() {
		super(Chalet.class);
	}

	public Chalet mangmentSavePost(Chalet obj, List<InputPart> inputParts) throws Exception {
		List<String> imagesUrl = uploadImagesMultiPart.uploadImagePost(inputParts);
		obj.setImages(imagesUrl);
		return this.save(obj);
	}

	public List<Chalet> findAllChaletWithFilter(User user, Village village, int page, int size, AtomicLong totalCount,
			District district, Governorate governorate, int minPrice, int maxPrice, Boolean pool, Boolean chimney)
			throws Exception {

		List<Chalet> lists;

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Chalet> criteriaQuery;
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

		criteriaQuery = criteriaBuilder.createQuery(Chalet.class);
		Root root = countQuery.from(Chalet.class);

		// Create a list of predicates based on your runtime conditions

		Predicate finalPredicate = buildPredicate(criteriaBuilder, root, user, village, district, governorate, minPrice,
				maxPrice, pool, chimney);

		criteriaQuery.where(finalPredicate);

		countQuery.select(criteriaBuilder.count(root)).where(finalPredicate);

		totalCount.set(getEntityManager().createQuery(countQuery).getSingleResult());

		criteriaQuery.multiselect(root).where(finalPredicate);

		TypedQuery<Chalet> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setHint("eclipselink.join-fetch", "RealEstate.village")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district.governorate")
				.setHint("eclipselink.join-fetch", "RealEstate.user");

		typedQuery.setFirstResult((page - 1) * size);
		typedQuery.setMaxResults(size);

		// Add predicates based on your conditions
		return typedQuery.getResultList();

	}

	private Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<Chalet> root, User user, Village village,
			District district, Governorate governorate, int minPrice, int maxPrice, Boolean pool, Boolean chimney)
			throws Exception {

		List<Predicate> predicates = new ArrayList<>();

		if (user != null) {
			predicates.add(criteriaBuilder.equal(root.get("user"), user));

		}

		if (chimney != null) {
			predicates.add(criteriaBuilder.equal(root.get("chimney"), chimney));

		}
		if (pool != null) {
			predicates.add(criteriaBuilder.equal(root.get("pool"), pool));

		}

		if (minPrice > 0) {

			Predicate minPricePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("weekdays"), minPrice);
			predicates.add(minPricePredicate);
		}

		if (maxPrice > 0) {
			Predicate maxPricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("weekenddays"), maxPrice);
			predicates.add(maxPricePredicate);
		}

		if (village != null) {
			predicates.add(criteriaBuilder.equal(root.get("village"), village));

		} else if (district != null) {
			predicates.add(criteriaBuilder.equal(root.get("village").get("district"), district));

		} else if (governorate != null) {
			predicates.add(criteriaBuilder.equal(root.get("village").get("district").get("governorate"), governorate));

		}

		Predicate postActive = criteriaBuilder.equal(root.get("postStatus"), PostStatus.ACCEPTED);
		predicates.add(postActive);

		// Combine the predicates using conjunction (AND) or disjunction (OR)
		Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));

		return finalPredicate;
	}

	// start here create predict
	// to use by kasssem
	public Long countChaletWithFilter(User user, Village village, int page, int size, AtomicLong totalCount,
			District district, Governorate governorate, int minPrice, int maxPrice, Boolean pool, Boolean chimney)
			throws Exception {

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

		CriteriaQuery<Chalet> criteriaQuery = criteriaBuilder.createQuery(Chalet.class);
		Root<Chalet> root = countQuery.from(Chalet.class);

		Predicate finalPredicate = buildPredicate(criteriaBuilder, root, user, village, district, governorate, minPrice,
				maxPrice, pool, chimney);

		criteriaQuery.where(finalPredicate);

		countQuery.select(criteriaBuilder.count(root)).where(finalPredicate);

		return getEntityManager().createQuery(countQuery).getSingleResult();

	}

	public List<Chalet> findUserChalets(User user) {
		TypedQuery<Chalet> query = getEntityManager().createNamedQuery(Chalet.FIND_POSTS_BY_USER_ID, Chalet.class);
		query.setParameter("user", user).getResultList();
		return query.getResultList();

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updatePostToExpiryDate(int nbOfDayExpireDate) {

		Query updateQuery = getEntityManager().createNamedQuery(Chalet.UPDATE_CHALET_TO_EXPIRY_DATE);
		updateQuery.setParameter("thresholdDate", Utils.getThresholdDate(nbOfDayExpireDate)); // Replace
																								// with
		// the threshold
		// date

		updateQuery.executeUpdate();

	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updatePostBoost() {

		Query updateQuery = getEntityManager().createNamedQuery(Chalet.UPDATE_POST_BOOST);
		updateQuery.setParameter("todayDate", new Date());

		updateQuery.executeUpdate();

	}

}

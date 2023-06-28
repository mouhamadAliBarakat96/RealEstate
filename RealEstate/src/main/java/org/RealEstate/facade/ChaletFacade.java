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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.RealEstate.service.UploadImagesMultiPart;
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
			District district, Governorate governorate, int weekdaysMinPrice, int weekdaysMaxPrice, Boolean pool,
			Boolean chimney, int weekenddaysMinPrice, int weekenddaysMaxPrice) throws Exception {

		List<Chalet> lists;

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Chalet> criteriaQuery;
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

		criteriaQuery = criteriaBuilder.createQuery(Chalet.class);
		Root root = countQuery.from(Chalet.class);

		// Create a list of predicates based on your runtime conditions

		Predicate finalPredicate = buildPredicate(criteriaBuilder, root, user, village, district, governorate,
				weekdaysMinPrice, weekdaysMaxPrice, pool, chimney, weekenddaysMinPrice, weekenddaysMaxPrice);

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
			District district, Governorate governorate, int weekdaysMinPrice, int weekdaysMaxPrice, Boolean pool,
			Boolean chimney, int weekenddaysMinPrice, int weekenddaysMaxPrice) throws Exception {

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

		if (weekdaysMinPrice > 0) {

			Predicate weekdaysMinPricePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("weekdays"), weekdaysMinPrice);
			predicates.add(weekdaysMinPricePredicate);
		}
		if (weekdaysMaxPrice > 0) {
			Predicate weekdaysMaxPricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("weekdays"), weekdaysMaxPrice);
			predicates.add(weekdaysMaxPricePredicate);
		}

		if (weekenddaysMinPrice > 0) {
			Predicate weekenddaysMinPricePredict = criteriaBuilder.greaterThanOrEqualTo(root.get("weekdays"), weekenddaysMinPrice);
			predicates.add(weekenddaysMinPricePredict);
		}
		if (weekenddaysMaxPrice > 0) {
			Predicate weekenddaysMaxPricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("weekdays"), weekenddaysMaxPrice);
			predicates.add(weekenddaysMaxPricePredicate);
		}

	
		if (weekenddaysMaxPrice > 0) {
			Predicate maxPricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("weekenddays"),
					weekenddaysMaxPrice);
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

}

package org.RealEstate.facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.RealEstate.enumerator.ExchangeRealEstateType;
import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.enumerator.PostType;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.Land;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.ShopRent;
import org.RealEstate.model.ShopSell;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utils;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.Expression;

@Stateless
public class RealEstateFacade extends AbstractFacade<RealEstate> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RealEstateFacade() {
		super(RealEstate.class);
	}

	public Long findUserCountPostPendingOrActive(Long userId) {
		return (Long) getEntityManager().createNamedQuery(RealEstate.FING_NB_POST_FOR_USER_ACTIVE_OR_PENDING)
				.setParameter("userId", userId).getSingleResult();
	}

	public Long findUserCountPostActive(Long userId) {
		return (Long) getEntityManager().createNamedQuery(RealEstate.FING_NB_POST_FOR_USER_ACTIVE)
				.setParameter("userId", userId).getSingleResult();
	}

	public Long findUserCountPostByStatus(PostStatus postStatus) {
		return (Long) getEntityManager().createNamedQuery(RealEstate.FIND_COUNT_POST_BY_STATUS)
				.setParameter("postStatus", postStatus).getSingleResult();
	}

	public Long findUserCountPostByType(PostType postType) {
		return (Long) getEntityManager().createNamedQuery(RealEstate.FIND_COUNT_POST_BY_TYPE)
				.setParameter("postType", postType).getSingleResult();
	}

	public RealEstate findWithQueryHint(Long id) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RealEstate> criteriaQuery = criteriaBuilder.createQuery(RealEstate.class);

		Root<RealEstate> root = criteriaQuery.from(RealEstate.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));

		TypedQuery<RealEstate> typedQuery = entityManager.createQuery(criteriaQuery);

		typedQuery.setHint("eclipselink.join-fetch", "RealEstate.village")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district.governorate")
				.setHint("eclipselink.join-fetch", "RealEstate.user");

		return typedQuery.getSingleResult();

	}

	public List<RealEstate> findAllRealSatateWithFilter(User user, boolean isAllPost, String postType, int minPrice,
			int maxPrice, Village village, int page, int size, AtomicLong totalCount, List<Integer> bedRoom,
			List<Integer> bathRoom, District district, Governorate governorate,
			ExchangeRealEstateType exchangeRealEstateType, List<String> sort) throws Exception {

		List<? extends RealEstate> realEstatesWithoutAddvertise;
		List<? extends RealEstate> realEstatesWithAddvertise;

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaBuilder criteriaBuilderWithAdd = getEntityManager().getCriteriaBuilder();

		CriteriaQuery<? extends RealEstate> criteriaQuery;
		CriteriaQuery<? extends RealEstate> criteriaQueryWithAdd;

		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		CriteriaQuery<Long> countQueryWithAdd = criteriaBuilderWithAdd.createQuery(Long.class);

		Root<? extends RealEstate> root;
		Class classType;
		if (postType != null) {

			classType = PostType.getEntityType(postType);

			if (classType == null) {
				throw new Exception(Constants.POST_TYPE_NOT_SUPPORTED);
			}

		} else {
			classType = RealEstate.class;
		}

		criteriaQuery = criteriaBuilder.createQuery(classType);
		criteriaQueryWithAdd = criteriaBuilderWithAdd.createQuery(classType);

		root = countQuery.from(classType);

		/**
		 * Sort
		 */

		Expression<?> orderExp = null;

		for (String var : sort) {
			String[] splitValues = var.split(",");
			String fieldName = splitValues[0];

			String rule = splitValues[1];
			orderExp = root.get(fieldName);
			if (rule.equalsIgnoreCase("asc")) {
				criteriaQuery.orderBy(criteriaBuilder.asc(orderExp));
				criteriaQueryWithAdd.orderBy(criteriaBuilderWithAdd.asc(orderExp));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(orderExp));
				criteriaQueryWithAdd.orderBy(criteriaBuilderWithAdd.desc(orderExp));

			}

		}

		// Create a list of predicates based on your runtime conditions
		Predicate finalPredicate = buildPredicate(criteriaBuilder, root, classType, user, isAllPost, postType, minPrice,
				maxPrice, village, totalCount, bedRoom, bathRoom, district, governorate, exchangeRealEstateType, sort);

		Predicate predicateBoostFalse = criteriaBuilder.equal(root.get("isBoosted"), false);
		Predicate finalPredicateWithoutAdd = criteriaBuilderWithAdd.and(finalPredicate, predicateBoostFalse);
		criteriaQuery.where(finalPredicateWithoutAdd);
		countQuery.select(criteriaBuilder.count(root)).where(finalPredicateWithoutAdd);

		Predicate predicateBoostTrue = criteriaBuilder.equal(root.get("isBoosted"), true);

		// jib li ma3 d3yt
		Predicate finalPredicateWithAdd = criteriaBuilderWithAdd.and(finalPredicate, predicateBoostTrue);
		countQueryWithAdd.select(criteriaBuilderWithAdd.count(root)).where(finalPredicateWithAdd);
		criteriaQueryWithAdd.where(finalPredicateWithAdd);
		criteriaQueryWithAdd.orderBy(criteriaBuilderWithAdd.asc(criteriaBuilderWithAdd.function("random", null))); // MySql
																													// RAND()
																													// function
		// used
		long totalCountWithadd = getEntityManager().createQuery(countQueryWithAdd).getSingleResult();

		long totalCountWithoutadd = getEntityManager().createQuery(countQuery).getSingleResult();

		totalCount.set(totalCountWithoutadd + totalCountWithadd);

		criteriaQuery.multiselect(root).where(finalPredicateWithoutAdd);

		TypedQuery<? extends RealEstate> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setHint("eclipselink.join-fetch", "RealEstate.village")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district.governorate")
				.setHint("eclipselink.join-fetch", "RealEstate.user");

		// hon bel size mnkhod 40/ma3mlon d3yat wel b2e bala d3ayet
		int withoutAdCount = (int) (size * 0.6);
		int withAdCount = size - withoutAdCount;

		if (withAdCount > totalCountWithadd) {

			int lifinZidon = (int) (withAdCount - totalCountWithadd);
			if (totalCountWithoutadd < (withoutAdCount + (withAdCount - totalCountWithadd))) {
				// check if total sho wad3o iza bikafo
				lifinZidon = (int) (totalCountWithoutadd - (withAdCount - totalCountWithadd));
			}

			withoutAdCount = (int) (withoutAdCount + lifinZidon);

		}

		// hon ana jbton kelon li bala d3yet hala2 bade jib lali ma3 di3ayet
		typedQuery.setFirstResult((page - 1) * withoutAdCount);
		typedQuery.setMaxResults(withoutAdCount);

		realEstatesWithoutAddvertise = typedQuery.getResultList();

		criteriaQueryWithAdd.multiselect(root).where(finalPredicateWithAdd);
		TypedQuery<? extends RealEstate> typedQueryWithBoost = getEntityManager().createQuery(criteriaQueryWithAdd);
		typedQuery.setHint("eclipselink.join-fetch", "RealEstate.village")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district.governorate")
				.setHint("eclipselink.join-fetch", "RealEstate.user");

		countQuery.select(criteriaBuilderWithAdd.count(root)).where(finalPredicateWithAdd);

		// hon ana jbton kelon li bala d3yet hala2 bade jib lali ma3 di3ayet
		typedQueryWithBoost.setMaxResults(withAdCount);

		realEstatesWithAddvertise = typedQueryWithBoost.getResultList();

		((List<RealEstate>) realEstatesWithoutAddvertise).addAll((List<RealEstate>) realEstatesWithAddvertise);

		return (List<RealEstate>) realEstatesWithoutAddvertise;
	}

	private Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<? extends RealEstate> root, Class classType,
			User user, boolean isAllPost, String postType, int minPrice, int maxPrice, Village village,
			AtomicLong totalCount, List<Integer> bedRoomList, List<Integer> bathRoomList, District district,
			Governorate governorate, ExchangeRealEstateType exchangeRealEstateType, List<String> sort)
			throws Exception {

		List<Predicate> predicates = new ArrayList<>();

		if (user != null) {
			predicates.add(criteriaBuilder.equal(root.get("user"), user));

		}

		if (exchangeRealEstateType != null && postType == null) {

			if (exchangeRealEstateType == ExchangeRealEstateType.BUY) {

				Predicate orPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("postType"), PostType.LAND),
						criteriaBuilder.equal(root.get("postType"), PostType.APPRATMENT_SELL),
						criteriaBuilder.equal(root.get("postType"), PostType.SHOP_SELL),
						criteriaBuilder.equal(root.get("postType"), PostType.OFFICE_SELL),
						criteriaBuilder.equal(root.get("postType"), PostType.STORE_HOUSE_SELL));
				predicates.add(orPredicate);
			} else if (exchangeRealEstateType == ExchangeRealEstateType.RENT) {
				Predicate orPredicate = criteriaBuilder.or(
						criteriaBuilder.equal(root.get("postType"), PostType.APPRATMENT_RENT),
						criteriaBuilder.equal(root.get("postType"), PostType.SHOP_RENT),
						criteriaBuilder.equal(root.get("postType"), PostType.OFFICE_RENT),
						criteriaBuilder.equal(root.get("postType"), PostType.STORE_HOUSE_RENT));

				predicates.add(orPredicate);
			}

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

		} else if (district != null) {
			predicates.add(criteriaBuilder.equal(root.get("village").get("district"), district));

		} else if (governorate != null) {
			predicates.add(criteriaBuilder.equal(root.get("village").get("district").get("governorate"), governorate));

		}

		List<Predicate> predicatesNbRoom = new ArrayList<>();

		// asln el land ma ha yje 3aded 8oraf bas ehtyat
		if (!classType.equals(Land.class) && !classType.equals(RealEstate.class) && !classType.equals(ShopSell.class)
				&& !classType.equals(ShopRent.class)

		) {

			if (bedRoomList != null && !bedRoomList.isEmpty()) {

				for (Integer var : bedRoomList) {
					Predicate maxPricePredicate = criteriaBuilder.equal(root.get("nbRoom"), var);

					predicatesNbRoom.add(maxPricePredicate);
				}

			}

			if (bathRoomList != null && !bathRoomList.isEmpty()) {

				for (Integer var : bathRoomList) {
					Predicate maxPricePredicate = criteriaBuilder.equal(root.get("nbBathRoom"), var);
					predicatesNbRoom.add(maxPricePredicate);
				}

			}

		}

		if (!isAllPost) {
			Predicate postActive = criteriaBuilder.equal(root.get("postStatus"), PostStatus.ACCEPTED);
			predicates.add(postActive);

		}

		// Combine the predicates using conjunction (AND) or disjunction (OR)
		Predicate finalPredicateAnd = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		Predicate finalPredicateOr = criteriaBuilder.or(predicatesNbRoom.toArray(new Predicate[0]));

		return criteriaBuilder.and(finalPredicateAnd, finalPredicateOr);

	}

	// start here create predict
	// to use by kasssem
	public Long countRealSatateWithFilter(User user, boolean isAllPost, String postType, int minPrice, int maxPrice,
			Village village, AtomicLong totalCount, List<Integer> bedRoom, List<Integer> bathRoom, District district,
			Governorate governorate, ExchangeRealEstateType exchangeRealEstateType, List<String> sort)
			throws Exception {

		CriteriaQuery<? extends RealEstate> criteriaQuery;

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<? extends RealEstate> root;
		Class classType;
		if (postType != null) {

			classType = PostType.getEntityType(postType);

			if (classType == null) {
				throw new Exception(Constants.POST_TYPE_NOT_SUPPORTED);
			}

		} else {
			classType = RealEstate.class;
		}

		criteriaQuery = criteriaBuilder.createQuery(classType);
		root = countQuery.from(classType);

		Predicate finalPredicate = buildPredicate(criteriaBuilder, root, classType, user, isAllPost, postType, minPrice,
				maxPrice, village, totalCount, bedRoom, bathRoom, district, governorate, exchangeRealEstateType, sort);

		criteriaQuery.where(finalPredicate);

		countQuery.select(criteriaBuilder.count(root)).where(finalPredicate);

		return getEntityManager().createQuery(countQuery).getSingleResult();

	}

	public Long findCountWithType(PostType postType) {
		return (Long) getEntityManager().createNamedQuery(RealEstate.FIND_COUNT_POST_BY_TYPE)
				.setParameter("postType", postType).getSingleResult();
	}

	public List<RealEstate> findUserRealEstates(User user) {
		TypedQuery<RealEstate> query = getEntityManager().createNamedQuery(RealEstate.FIND_POSTS_BY_USER_ID,
				RealEstate.class);
		query.setParameter("user", user).getResultList();
		return query.getResultList();

	}

	/**
	 * Set post expire date After certain time
	 */

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updatePostToExpiryDate(int nbOfDayExpireDate) {

		Query updateQuery = getEntityManager().createNamedQuery(RealEstate.UPDATE_POST_TO_EXPIRY_DATE);
		updateQuery.setParameter("thresholdDate", Utils.getThresholdDate(nbOfDayExpireDate));

		updateQuery.executeUpdate();

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updatePostBoost() {

		Query updateQuery = getEntityManager().createNamedQuery(RealEstate.UPDATE_POST_BOOST);
		updateQuery.setParameter("todayDate", new Date());

		updateQuery.executeUpdate();

	}

}

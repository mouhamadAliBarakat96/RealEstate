package org.RealEstate.LazyDataModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.enumerator.PostType;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.Village;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

public class LazyPostModel extends LazyDataModel<RealEstate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RealEstateFacade realEstateFacade;

	// out fillter

	private Date fromDate;
	private Date toDate;
	private Governorate governorate;
	private District district;
	private Village village;
	private int minPrice;
	private int maxPrice;

	public LazyPostModel(RealEstateFacade realEstateFacade, Date fromDate, Date toDate, Governorate governorate,
			District district, Village village, int minPrice, int maxPrice) {

		this.realEstateFacade = realEstateFacade;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.governorate = governorate;
		this.district = district;
		this.village = village;
		this.minPrice = minPrice;

		this.maxPrice = maxPrice;

	}

	@Override
	public int count(Map<String, FilterMeta> filterBy) {

		CriteriaBuilder criteriaBuilder = realEstateFacade.getEm().getCriteriaBuilder();
		CriteriaQuery<RealEstate> criteriaQuery;
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

		Root<RealEstate> root = countQuery.from(RealEstate.class);

		// Apply filters
		Predicate finalPredicate = applyFilters(criteriaBuilder, root, filterBy);
		criteriaQuery = criteriaBuilder.createQuery(RealEstate.class);
		root = countQuery.from(RealEstate.class);

		criteriaQuery.where(finalPredicate);

		countQuery.select(criteriaBuilder.count(root)).where(finalPredicate);

		return Math.toIntExact(realEstateFacade.getEm().createQuery(countQuery).getSingleResult());
	}

	@Override
	public String getRowKey(RealEstate realEstate) {
		return String.valueOf(realEstate.getId());
	}

	private Predicate applyFilters(CriteriaBuilder cb, Root<RealEstate> root, Map<String, FilterMeta> filterBy) {
		List<Predicate> predicates = new ArrayList<>();
		for (Map.Entry<String, FilterMeta> entry : filterBy.entrySet()) {
			String field = entry.getKey();
			FilterMeta filterMeta = entry.getValue();
			Object filterValue = filterMeta.getFilterValue();

			if (filterValue != null) {

				if ("postType".equals(field)) {
					PostType postTypeEnum = PostType.findEnum(filterValue.toString());
					predicates.add(cb.equal(root.get("postType"), postTypeEnum));

				}

				else if ("id".equals(field)) {

					predicates.add(cb.like(cb.function("text", String.class, root.get("id")), "%" + filterValue + "%"));

				}

				else if ("postStatus".equals(field)) {
					PostStatus postStatus = PostStatus.findEnum(filterValue.toString());
					predicates.add(cb.equal(root.get("postStatus"), postStatus));

				} else if ("pricePublic".equals(field)) {

					predicates.add(cb.equal(root.get("pricePublic"), filterValue));

				}

				// Add more conditions for other fields as needed
			}

		}

		if (village != null) {
			predicates.add(cb.equal(root.get("village"), village));

		} else if (district != null) {
			predicates.add(cb.equal(root.get("village").get("district"), district));

		} else if (governorate != null) {
			predicates.add(cb.equal(root.get("village").get("district").get("governorate"), governorate));

		}

		if (minPrice > 0) {

			Predicate minPricePredicate = cb.greaterThanOrEqualTo(root.get("price"), minPrice);
			predicates.add(minPricePredicate);
		}
		if (maxPrice > 0) {
			Predicate maxPricePredicate = cb.lessThanOrEqualTo(root.get("price"), maxPrice);
			predicates.add(maxPricePredicate);
		}

		if (fromDate != null) {
			Predicate datePredicateFrom = cb.greaterThanOrEqualTo(root.<Date>get("postDate"), fromDate);
			predicates.add(datePredicateFrom);

		}
		if (toDate != null) {
			Predicate datePredicateTo = cb.lessThanOrEqualTo(root.get("postDate"), toDate);

			predicates.add(datePredicateTo);
		}
		return cb.and(predicates.toArray(new Predicate[0]));

	}

	@Override
	public List<RealEstate> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
		CriteriaBuilder criteriaBuilder = realEstateFacade.getEm().getCriteriaBuilder();

		CriteriaQuery<RealEstate> criteriaQuery = criteriaBuilder.createQuery(RealEstate.class);

		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

		Root<RealEstate> root = criteriaQuery.from(RealEstate.class);

		// Apply filters
		Predicate finalPredicate = applyFilters(criteriaBuilder, root, filterBy);
		criteriaQuery.where(finalPredicate);
		// Apply sorting
		applySorting(criteriaQuery, criteriaBuilder, root, sortBy);

		countQuery.select(criteriaBuilder.count(root)).where(finalPredicate);

		int totalCount = Math.toIntExact((realEstateFacade.getEm().createQuery(countQuery).getSingleResult()));

		TypedQuery<? extends RealEstate> typedQuery = realEstateFacade.getEm().createQuery(criteriaQuery);
		typedQuery.setHint("eclipselink.join-fetch", "RealEstate.village")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district.governorate")
				.setHint("eclipselink.join-fetch", "RealEstate.user");

		typedQuery.setFirstResult(first);
		typedQuery.setMaxResults(pageSize);
		List<? extends RealEstate> realEstates = typedQuery

				.getResultList();

		// Set the total count and return the loaded customers
		setRowCount(totalCount);
		return (List<RealEstate>) realEstates;
	}

	private void applySorting(CriteriaQuery<RealEstate> query, CriteriaBuilder cb, Root<RealEstate> root,
			Map<String, SortMeta> sortBy) {
		sortBy.forEach((field, sortMeta) -> {
			if (sortMeta.getOrder().equals(SortOrder.ASCENDING)) {
				query.orderBy(cb.asc(root.get(field)));
			} else {
				query.orderBy(cb.desc(root.get(field)));
			}
		});
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Governorate getGovernorate() {
		return governorate;
	}

	public void setGovernorate(Governorate governorate) {
		this.governorate = governorate;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Village getVillage() {
		return village;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	public int getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(int minPrice) {
		this.minPrice = minPrice;
	}

	public int getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}

}

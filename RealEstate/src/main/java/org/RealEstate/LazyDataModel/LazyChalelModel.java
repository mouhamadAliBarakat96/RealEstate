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
import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.Village;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

public class LazyChalelModel extends LazyDataModel<Chalet> {
	private static final long serialVersionUID = 1L;

	private ChaletFacade chaletFacade;

	private Date fromDate;
	private Date toDate;
	private Governorate governorate;
	private District district;
	private Village village;
	private int minPrice;
	private int maxPrice;

	public LazyChalelModel(ChaletFacade chaletFacade, Date fromDate, Date toDate, Governorate governorate,
			District district, Village village) {

		this.chaletFacade = chaletFacade;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.governorate = governorate;
		this.district = district;
		this.village = village;

	}

	@Override
	public int count(Map<String, FilterMeta> filterBy) {

		CriteriaBuilder criteriaBuilder = chaletFacade.getEm().getCriteriaBuilder();
		CriteriaQuery<Chalet> criteriaQuery;
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

		Root<Chalet> root = countQuery.from(Chalet.class);

		// Apply filters
		Predicate finalPredicate = applyFilters(criteriaBuilder, root, filterBy);
		criteriaQuery = criteriaBuilder.createQuery(Chalet.class);
		root = countQuery.from(Chalet.class);

		criteriaQuery.where(finalPredicate);

		countQuery.select(criteriaBuilder.count(root)).where(finalPredicate);

		return Math.toIntExact(chaletFacade.getEm().createQuery(countQuery).getSingleResult());
	}

	@Override
	public String getRowKey(Chalet chalet) {
		return String.valueOf(chalet.getId());
	}

	private Predicate applyFilters(CriteriaBuilder cb, Root<Chalet> root, Map<String, FilterMeta> filterBy) {
		List<Predicate> predicates = new ArrayList<>();
		for (Map.Entry<String, FilterMeta> entry : filterBy.entrySet()) {
			String field = entry.getKey();
			FilterMeta filterMeta = entry.getValue();
			Object filterValue = filterMeta.getFilterValue();

			if (filterValue != null) {

				if ("id".equals(field)) {

					predicates.add(cb.like(cb.function("text", String.class, root.get("id")), "%" + filterValue + "%"));

				}

				else if ("postStatus".equals(field)) {
					PostStatus postStatus = PostStatus.findEnum(filterValue.toString());
					predicates.add(cb.equal(root.get("postStatus"), postStatus));

				}
				else if ("pendingBoost".equals(field)) {
					
					predicates.add(cb.equal(root.get("pendingBoost"), filterValue));
					
					
			

				}

				
				
				
				
				
				
			}

		}

		if (village != null) {
			predicates.add(cb.equal(root.get("village"), village));

		} else if (district != null) {
			predicates.add(cb.equal(root.get("village").get("district"), district));

		} else if (governorate != null) {
			predicates.add(cb.equal(root.get("village").get("district").get("governorate"), governorate));

		}

		if (fromDate != null) {
			Predicate datePredicateFrom = cb.lessThanOrEqualTo(root.<Date>get("postDate"), fromDate);
			predicates.add(datePredicateFrom);

		}
		if (toDate != null) {
			Predicate datePredicateTo = cb.greaterThanOrEqualTo(root.get("postDate"), toDate);

			predicates.add(datePredicateTo);
		}
		return cb.and(predicates.toArray(new Predicate[0]));

		
	}
	
	@Override
	public List<Chalet> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
		CriteriaBuilder criteriaBuilder = chaletFacade.getEm().getCriteriaBuilder();

		CriteriaQuery<Chalet> criteriaQuery = criteriaBuilder.createQuery(Chalet.class);

		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

		Root<Chalet> root = criteriaQuery.from(Chalet.class);

		// Apply filters
		Predicate finalPredicate = applyFilters(criteriaBuilder, root, filterBy);
		criteriaQuery.where(finalPredicate);
		// Apply sorting
		applySorting(criteriaQuery, criteriaBuilder, root, sortBy);

		countQuery.select(criteriaBuilder.count(root)).where(finalPredicate);

		int totalCount = Math.toIntExact((chaletFacade.getEm().createQuery(countQuery).getSingleResult()));

		TypedQuery<? extends Chalet> typedQuery = chaletFacade.getEm().createQuery(criteriaQuery);
		typedQuery.setHint("eclipselink.join-fetch", "Chalet.village")
				.setHint("eclipselink.join-fetch", "Chalet.village.district")
				.setHint("eclipselink.join-fetch", "Chalet.village.district.governorate")
				.setHint("eclipselink.join-fetch", "Chalet.user");

		typedQuery.setFirstResult(first);
		typedQuery.setMaxResults(pageSize);
		List<? extends Chalet> chalets = typedQuery

				.getResultList();

		// Set the total count and return the loaded customers
		setRowCount(totalCount);
		return (List<Chalet>) chalets;
	}
	
	
	private void applySorting(CriteriaQuery<Chalet> query, CriteriaBuilder cb, Root<Chalet> root,
			Map<String, SortMeta> sortBy) {
		sortBy.forEach((field, sortMeta) -> {
			if (sortMeta.getOrder().equals(SortOrder.ASCENDING)) {
				query.orderBy(cb.asc(root.get(field)));
			} else {
				query.orderBy(cb.desc(root.get(field)));
			}
		});
	}

}

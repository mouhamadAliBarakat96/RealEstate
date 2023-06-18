package org.RealEstate.LazyDataModel;

import java.util.ArrayList;
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
import org.RealEstate.model.RealEstate;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

public class LazyPostCustomerModel extends LazyDataModel<RealEstate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RealEstateFacade realEstateFacade;

	public LazyPostCustomerModel(RealEstateFacade realEstateFacade) {
		this.setRowCount(10);
		this.realEstateFacade = realEstateFacade;
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
		Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[0]));

		return finalPredicate;
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
}

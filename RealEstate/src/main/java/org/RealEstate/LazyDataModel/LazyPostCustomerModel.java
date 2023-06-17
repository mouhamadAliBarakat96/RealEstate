package org.RealEstate.LazyDataModel;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
		CriteriaBuilder cb = realEstateFacade.getEm().getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<RealEstate> root = query.from(RealEstate.class);

		// Apply filters
		Predicate predicate = applyFilters(cb, root, filterBy);

		// Count the total number of customers
		Long count = countPosts(predicate);

		return count.intValue();
	}

	@Override
	public String getRowKey(RealEstate realEstate) {
		return String.valueOf(realEstate.getId());
	}

	private Predicate applyFilters(CriteriaBuilder cb, Root<RealEstate> root, Map<String, FilterMeta> filterBy) {
		Predicate predicate = cb.conjunction();
		for (Map.Entry<String, FilterMeta> entry : filterBy.entrySet()) {
			String field = entry.getKey();
			FilterMeta filterMeta = entry.getValue();
			Object filterValue = filterMeta.getFilterValue();

			if (filterValue != null) {
				if ("fieldName".equals(field)) {
					// Apply filtering logic for "fieldName"
					// Example: predicate = cb.and(predicate, cb.equal(root.get("fieldName"),
					// filterValue));
				} else if ("anotherField".equals(field)) {
					// Apply filtering logic for "anotherField"
					// Example: predicate = cb.and(predicate, cb.like(root.get("anotherField"), "%"
					// + filterValue + "%"));
				}
				// Add more conditions for other fields as needed
			}
		}
		return predicate;
	}

	private Long countPosts(Predicate predicate) {
		CriteriaBuilder cb = realEstateFacade.getEm().getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<RealEstate> root = countQuery.from(RealEstate.class);
		countQuery.select(cb.count(root));
		countQuery.where(predicate);
		return realEstateFacade.getEm().createQuery(countQuery).getSingleResult();
	}

	@Override
	public List<RealEstate> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
		CriteriaBuilder cb = realEstateFacade.getEm().getCriteriaBuilder();
		CriteriaQuery<RealEstate> query = cb.createQuery(RealEstate.class);
		Root<RealEstate> root = query.from(RealEstate.class);

		// Apply filters
		Predicate predicate = applyFilters(query, cb, root, filterBy);

		// Apply sorting
		applySorting(query, cb, root, sortBy);

		// Count the total number of customers
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		countQuery.select(cb.count(countQuery.from(RealEstate.class)));
		countQuery.where(predicate);
		Long count = realEstateFacade.getEm().createQuery(countQuery).getSingleResult();

		// Load the customers with pagination
		List<RealEstate> customers = realEstateFacade.getEm().createQuery(query).setFirstResult(first)
				.setMaxResults(pageSize).setHint("eclipselink.join-fetch", "RealEstate.village")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district")
				.setHint("eclipselink.join-fetch", "RealEstate.village.district.governorate")
				.setHint("eclipselink.join-fetch", "RealEstate.user").getResultList();

		// Set the total count and return the loaded customers
		setRowCount(count.intValue());
		return customers;
	}

	private Predicate applyFilters(CriteriaQuery<RealEstate> query, CriteriaBuilder cb, Root<RealEstate> root,
			Map<String, FilterMeta> filterBy) {
		Predicate predicate = cb.conjunction();
// Apply your filters logic here
// Example: FilterMeta filterMeta = filterBy.get("fieldName");
//          if (filterMeta != null && filterMeta.getFilterValue() != null) {
//              String filterValue = filterMeta.getFilterValue().toString();
//              predicate = cb.and(predicate, cb.like(root.get("fieldName"), "%" + filterValue + "%"));
//          }
		return predicate;
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

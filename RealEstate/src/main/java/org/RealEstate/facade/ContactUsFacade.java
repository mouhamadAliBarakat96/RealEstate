package org.RealEstate.facade;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.RealEstate.model.ContactUs;
import org.RealEstate.model.User;

@Stateless
public class ContactUsFacade extends AbstractFacade<ContactUs> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContactUsFacade() {
		super(ContactUs.class);
	}

	public void deleteByUserId(User user) {
		try {
			List<ContactUs> contacts = findContactsByUser(user);
			if (contacts != null && contacts.size() > 1) {
				remove(contacts);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ContactUs> findContactsByUser(User user) {
		try {
			TypedQuery<ContactUs> query = em.createNamedQuery(ContactUs.FIND_BY_USER, ContactUs.class);
			query.setParameter("pUser", user);
			List<ContactUs> contacts = query.getResultList();
			return contacts;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
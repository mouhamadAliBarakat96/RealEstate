package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.ContactUs;


@Stateless
public class ContactUsFacade extends AbstractFacade<ContactUs> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContactUsFacade() {
		super(ContactUs.class);
	}
}
package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.User;
@Stateless
public class UserFacade extends AbstractFacade<User> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public UserFacade() {
		super(User.class);
	}
}

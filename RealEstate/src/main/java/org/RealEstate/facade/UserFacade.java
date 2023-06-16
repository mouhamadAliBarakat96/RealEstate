package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.User;
import org.RealEstate.utils.Constants;
@Stateless
public class UserFacade extends AbstractFacade<User> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public UserFacade() {
		super(User.class);
	}
	
	
	public User findWithExcption(Long id) throws Exception {
		User user = this.find(id);
		if(user !=null) {
			return user;
		}
		else {
			throw new Exception(Constants.USER_NOT_EXISTS);
		}
	}
	
	
	
	
}

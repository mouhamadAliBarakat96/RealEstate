package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.RealEstate;

@Stateless
public class RealEstateFacade extends AbstractFacade<RealEstate> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RealEstateFacade() {
		super(RealEstate.class);
	}

	public Long  findUserCountPost(Long userId) {
	return	(Long)  getEntityManager().createNamedQuery(RealEstate.FING_NB_POST_FOR_USER)
				.setParameter("userId", userId)
				.getSingleResult();
	}

}

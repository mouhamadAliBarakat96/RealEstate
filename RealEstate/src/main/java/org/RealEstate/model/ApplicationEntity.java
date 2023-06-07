package org.RealEstate.model;

import java.io.Serializable;

public abstract class ApplicationEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract long getId();

	public abstract void setId(long id);

}

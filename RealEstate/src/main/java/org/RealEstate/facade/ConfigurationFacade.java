package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.District;
import org.RealEstate.model.GeneralConfiguration;



@Stateless

public class ConfigurationFacade extends AbstractFacade<GeneralConfiguration> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigurationFacade() {
		super(GeneralConfiguration.class);
	}

}
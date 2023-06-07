package org.RealEstate.model;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;


public class MainEntityListener {

	@PrePersist
	public void creationDate(MainEntity mainEntity) {
		if (mainEntity.getCreationDate() == null) {
			mainEntity.setCreationDate(new Date());
			mainEntity.setLastUpdate(new Date());

		}
	}

	@PreUpdate
	public void lastUpdate(MainEntity entity) {
		entity.setLastUpdate(new Date());
	}

}

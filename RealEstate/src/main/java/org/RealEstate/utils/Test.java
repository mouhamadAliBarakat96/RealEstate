package org.RealEstate.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.model.AppratmentRent;
import org.RealEstate.model.Land;
import org.RealEstate.model.RealEstate;

@ViewScoped
@Named
public class Test implements Serializable {
	private static final long serialVersionUID = 1L;
	List<RealEstate> list = new ArrayList<>();

	@PostConstruct
	public void init() {

		Land land = new Land();
		land.setElectricity(true);
		AppratmentRent appratmentRent = new AppratmentRent();
		appratmentRent.setFloor(2);
		list.add(land);
		list.add(appratmentRent);
		appratmentRent = (AppratmentRent) list.get(1);
		System.out.println(appratmentRent.getFloor());
	}

	public List<RealEstate> getList() {
		return list;
	}

	public void setList(List<RealEstate> list) {
		this.list = list;
	}

}

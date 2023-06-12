package org.RealEstate.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.model.AppratmentRent;
import org.RealEstate.model.AppratmentSell;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.GoogleMapAttribute;
import org.RealEstate.model.Land;
import org.RealEstate.model.OfficeRent;
import org.RealEstate.model.OfficeSell;
import org.RealEstate.model.RealEstate;
/*
 * @ViewScoped
 * 
 * @Named public class Test implements Serializable { private static final long
 * serialVersionUID = 1L; List<RealEstate> list = new ArrayList<>();
 * 
 * @PostConstruct public void init() {
 * 
 * Land land = new Land(); land.setElectricity(true); AppratmentRent
 * appratmentRent = new AppratmentRent(); appratmentRent.setFloor(2);
 * addCommonsField(appratmentRent);
 * 
 * list.add(land); list.add(appratmentRent); appratmentRent = (AppratmentRent)
 * list.get(1); System.out.println(appratmentRent.getFloor());
 * System.out.println(appratmentRent.getId()); }
 * 
 * private static void addCommonsField(RealEstate realEstate) {
 * realEstate.setId(123); }
 * 
 * public List<RealEstate> getList() { return list; }
 * 
 * public void setList(List<RealEstate> list) { this.list = list; }
 * 
 * }
 */
import org.RealEstate.model.ShopRent;
import org.RealEstate.model.ShopSell;

public class Test {
	public static void main(String[] args) throws IOException {
		try {
			AppratmentRent appratmentRent = new AppratmentRent();

			System.out.println("AppratmentRent");
			System.out.println(Utils.objectToString(appratmentRent));
			AppratmentSell appratmentSell = new AppratmentSell();
			System.out.println("appratmentSell");
			System.out.println(Utils.objectToString(appratmentSell));
			Land land = new Land();
			System.out.println("Land");
			System.out.println(Utils.objectToString(land));
			OfficeRent officeRent = new OfficeRent();
			System.out.println("OfficeRent");
			System.out.println(Utils.objectToString(officeRent));

			OfficeSell officeSell = new OfficeSell();
			System.out.println("OfficeSell");
			System.out.println(Utils.objectToString(officeSell));
			ShopRent shopRent = new ShopRent();
			System.out.println("ShopRent");
			System.out.println(Utils.objectToString(shopRent));
			ShopSell shopSell = new ShopSell();
			System.out.println("ShopSell");
			System.out.println(Utils.objectToString(shopSell));

			Chalet chalet = new Chalet();
			System.out.println("Chalet");
			System.out.println(Utils.objectToString(chalet));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

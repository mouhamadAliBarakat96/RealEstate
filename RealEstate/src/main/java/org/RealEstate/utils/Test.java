package org.RealEstate.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.enumerator.PostType;
import org.RealEstate.enumerator.WaterResources;
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
import org.RealEstate.model.User;
import org.RealEstate.model.Village;

public class Test {
	public static void main(String[] args) throws IOException {
		try {

		
			
			List<String> xxx = new ArrayList<String>();

			xxx.add("me_-824870707.jpg");
			xxx.add("me_-824870707.jpg");
			System.out.println(Utils.listToString(xxx));

			Boolean x = null;

			System.out.println(x);

			List<WaterResources> waterResources = new ArrayList<>();
			waterResources.add(WaterResources.CALCAREOUS_WATER);

			Village village = new Village();
			village.setId(701);

			AppratmentRent appratmentRent = new AppratmentRent();
			appratmentRent.setPostType(PostType.APPRATMENT_RENT);
			appratmentRent.setWaterResources(waterResources);
			appratmentRent.setVillage(village);
			appratmentRent.setTittle("dqwdqw");
			appratmentRent.setSubTittle("dqwdqw");

			System.out.println("AppratmentRent");
			System.out.println(Utils.objectToString(appratmentRent));
			AppratmentSell appratmentSell = new AppratmentSell();
			System.out.println("appratmentSell");
			appratmentSell.setPostType(PostType.APPRATMENT_SELL);
			appratmentSell.setWaterResources(waterResources);
			appratmentSell.setVillage(village);
			System.out.println(Utils.objectToString(appratmentSell));
			Land land = new Land();
			land.setPostType(PostType.LAND);
			// land.setWaterResources(waterResources);
			land.setVillage(village);
			System.out.println("Land");
			System.out.println(Utils.objectToString(land));
			OfficeRent officeRent = new OfficeRent();
			System.out.println("OfficeRent");
			officeRent.setPostType(PostType.OFFICE_RENT);
			officeRent.setWaterResources(waterResources);
			officeRent.setVillage(village);
			System.out.println(Utils.objectToString(officeRent));

			OfficeSell officeSell = new OfficeSell();
			System.out.println("OfficeSell");
			officeSell.setPostType(PostType.OFFICE_SELL);
			officeSell.setWaterResources(waterResources);
			officeSell.setVillage(village);
			System.out.println(Utils.objectToString(officeSell));
			ShopRent shopRent = new ShopRent();
			System.out.println("ShopRent");
			shopRent.setPostType(PostType.SHOP_RENT);
			shopRent.setWaterResources(waterResources);
			shopRent.setVillage(village);
			System.out.println(Utils.objectToString(shopRent));
			ShopSell shopSell = new ShopSell();
			System.out.println("ShopSell");
			shopSell.setPostType(PostType.SHOP_SELL);
			shopSell.setWaterResources(waterResources);
			shopSell.setVillage(village);
			System.out.println(Utils.objectToString(shopSell));

			Chalet chalet = new Chalet();
			chalet.setVillage(village);
			System.out.println("Chalet");
			System.out.println(Utils.objectToString(chalet));
			System.out.println("add post type to json");

			User user = new User();
			user.setFirstName("dada");
			user.setLastName("dada");
			user.setMiddleName("dada");
			user.setUserName("userName");
			user.setPassowrd(Utils.sha256("123456"));
			user.setPhoneNumber("71006196");
			System.out.println("<<>>");
			System.out.println("USER");
			System.out.println(Utils.objectToString(user));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

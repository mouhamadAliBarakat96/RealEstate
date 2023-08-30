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
import org.RealEstate.model.StoreHouseRent;
import org.RealEstate.model.StoreHouseSell;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;

public class Test {
	
	public static String replaceHost(String originalUrl, String newHost) {
	    int startIndex = originalUrl.indexOf("://") + 3; // Find the start of the host
	    int endIndex = originalUrl.indexOf("/", startIndex); // Find the end of the host or the start of the path

	    if (startIndex >= 0 && endIndex >= 0) {
	        String oldHostAndPort = originalUrl.substring(startIndex, endIndex);
	        String[] parts = oldHostAndPort.split(":", 2); // Split host and port
	        String oldHost = parts[0];

	        if (parts.length > 1) {
	            // Check if the part after colon is a valid port
	            String portPart = parts[1];
	            if (portPart.matches("\\d{1,4}")) {
	                // If valid port, replace host and port with new host
	                return originalUrl.replace(oldHostAndPort, newHost);
	            }
	        } else {
	            // If no port, replace only host with new host
	            return originalUrl.replace(oldHost, newHost);
	        }
	    }

	    return originalUrl;
	}
	
	public static void main(String[] args) throws IOException {
		
		try {
		String x = 	replaceHost("https://ekarplus.com:8080/iews/governorate/governorate.xhtml?id=34102" , "localhosost");
System.out.println(x);
		
		System.out.println(Utils.addDaysToCurrentDate(7));	
		

			List<WaterResources> waterResources = new ArrayList<>();
			waterResources.add(WaterResources.CALCAREOUS_WATER);

			Village village = new Village();
			village.setId(102);

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
			
			System.out.println(Utils.objectToString(user));

			
			
			 StoreHouseSell  storeHouseSell = new StoreHouseSell();
			System.out.println("storeHouseSell");
			storeHouseSell.setPostType(PostType.OFFICE_SELL);
			storeHouseSell.setWaterResources(waterResources);
			storeHouseSell.setVillage(village);
			
			
			System.out.println(Utils.objectToString(storeHouseSell));

			 StoreHouseRent  storeHouseRent = new StoreHouseRent();
			System.out.println("storeHouserent");
			storeHouseRent.setPostType(PostType.OFFICE_SELL);
			storeHouseRent.setWaterResources(waterResources);
			storeHouseRent.setVillage(village);
			System.out.println(Utils.objectToString(storeHouseRent));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

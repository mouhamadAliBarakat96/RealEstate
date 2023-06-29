package org.RealEstate.dto;

import java.util.ArrayList;
import java.util.List;

import org.RealEstate.enumerator.PropertyKindEnum;


public class MenuItem {

	private String itemLabel;
	private String iconName;
	private String Url;
	private boolean permission;
	private String permissionName;
	private String subMenuTitle;
	private List<MenuItem> subMenuDetails = new ArrayList<MenuItem>();
	private String styleClass ;
	private String kindEnum;
	
	private String href; 
	
	 

	public MenuItem(String itemLabel, String iconName, String url, boolean permission, String subMenuTitle,
			String styleClass, String href) {
		
		super();
		this.itemLabel = itemLabel;
		this.iconName = iconName;
		Url = url;
		this.permission = permission;
		this.subMenuTitle = subMenuTitle;
		this.styleClass = styleClass;
		this.href = href;
	}
	public MenuItem(String href ,String itemLabel, String iconName, String url, String subMenuTitle, boolean permission) {
		super();
		this.href = href;
		this.itemLabel = itemLabel;
		this.iconName = iconName;
		Url = url;
		this.subMenuTitle = subMenuTitle;
		this.permission = permission;

	}
	/*constructor to add card */
	public MenuItem(String itemLabel, String iconName, String url, boolean permission,String styleClass) {
		super();
		this.itemLabel = itemLabel;
		this.iconName = iconName;
		this.Url = url;
		this.permission = permission;
		this.styleClass = styleClass;
	}
	
	public MenuItem(String itemLabel, String iconName, String url, boolean permission,String styleClass, String kindEnum) {
		super();
		this.itemLabel = itemLabel;
		this.iconName = iconName;
		this.Url = url;
		this.permission = permission;
		this.styleClass = styleClass;
		this.kindEnum = kindEnum;

	}


	public MenuItem(String itemLabel, String iconName, String url, String subMenuTitle, boolean permission,
			String permissionName) {
		super();
		this.itemLabel = itemLabel;
		this.iconName = iconName;
		Url = url;
		this.subMenuTitle = subMenuTitle;
		this.permission = permission;
		this.permissionName = permissionName;
	}

	public String getItemLabel() {
		return itemLabel;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getSubMenuTitle() {
		return subMenuTitle;
	}

	public void setSubMenuTitle(String subMenuTitle) {
		this.subMenuTitle = subMenuTitle;
	}

	public boolean isPermission() {
		return permission;
	}

	public void setPermission(boolean permission) {
		this.permission = permission;
	}

	public List<MenuItem> getSubMenuDetails() {
		return subMenuDetails;
	}

	public void setSubMenuDetails(List<MenuItem> subMenuDetails) {
		this.subMenuDetails = subMenuDetails;
	}

	public void addSubMenu(List<MenuItem> subMenu) {
		this.subMenuDetails = subMenu;
	}

	public void addChild(MenuItem menuItem) {
		this.subMenuDetails.add(menuItem);
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getKindEnum() {
		return kindEnum;
	}
	public void setKindEnum(String kindEnum) {
		this.kindEnum = kindEnum;
	}
}

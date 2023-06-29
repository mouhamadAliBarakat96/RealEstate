package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.controller.AbstractController;
import org.RealEstate.enumerator.PostType;
import org.RealEstate.enumerator.PropertyKindEnum;
import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.facade.VillageFacade;
import org.RealEstate.interfaces.ICRUDOperations;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.RealEstate.utils.Utility;
import org.omnifaces.util.Faces;
import org.primefaces.event.TabChangeEvent;

@Named
@ViewScoped
public class UserPostCardController extends AbstractController<RealEstate> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String REQUEST_PARAM = "id";

	private PostType postType;
	private PropertyKindEnum kindEnum;

	@Inject
	private ChaletFacade chaletFacade;
	@Inject
	private RealEstateFacade estateFacade;
	@Inject
	private VillageFacade villageFacade;

	private List<Village> villages = new ArrayList<Village>();

	private RealEstate item;
	private Chalet chalet;

	private User user;

	@PostConstruct
	public void init() {
		villages = villageFacade.findAll();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		if (!facesContext.isPostback()) {
			String id = externalContext.getRequestParameterMap().get(REQUEST_PARAM);
			if (id != null && Long.parseLong(id) > 0) {
				item = find(Long.parseLong(id));
				if (item == null) {
					try {
						Faces.redirect("/error.xhtml");
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				if (flash.containsKey("new-card")) {
					Utility.addSuccessMessage("save_success");
				}
			} else {
				if (postType != null)
					item = Utility.initializeRealEstate(postType);
			}

		}
	}

	public void listenerSelectItemType() {
		if (postType != null) {
			item = Utility.initializeRealEstate(postType);
		}
	}

	public void onTabChange(TabChangeEvent event) {
		switch (event.getTab().getId()) {
		case "realestate":
			kindEnum = PropertyKindEnum.REALESTATE;
			break;
		case "chalet":
			kindEnum = PropertyKindEnum.CHALET;
			break;
		default:
			break;
		}
	}

	public void save() {
		try {

			if (getItem().getId() <= 0) {

				item.setPostDate(new Date());
				item.setUser(user);

				super.save();
				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				flash.put("new-card", "true");
				changeUrl();
			} else {
				item = getAbstractFacade().save(item);
			}
			Utility.addSuccessMessage("save_success");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void changeUrl() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		try {
			Faces.redirect(url + "?" + REQUEST_PARAM + "=%s", getItem().getId() + "");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public RealEstate getItem() {
		// TODO Auto-generated method stub
		return item;
	}

	@Override
	public void setItem(RealEstate item) {
		// TODO Auto-generated method stub
		this.item = item;
	}

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return item.getId();
	}

	@Override
	public ICRUDOperations<RealEstate> getAbstractFacade() {
		// TODO Auto-generated method stub
		return estateFacade;
	}

	public PostType getPostType() {
		return postType;
	}

	public void setPostType(PostType postType) {
		this.postType = postType;
	}

	public List<Village> getVillages() {
		return villages;
	}

	public void setVillages(List<Village> villages) {
		this.villages = villages;
	}

	public Chalet getChalet() {
		return chalet;
	}

	public void setChalet(Chalet chalet) {
		this.chalet = chalet;
	}

	public PropertyKindEnum getKindEnum() {
		return kindEnum;
	}

	public void setKindEnum(PropertyKindEnum kindEnum) {
		this.kindEnum = kindEnum;
	}

}

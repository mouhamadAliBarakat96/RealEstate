package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;

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
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.interfaces.ICRUDOperations;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.User;
import org.RealEstate.utils.Utility;
import org.omnifaces.util.Faces;

@Named
@ViewScoped
public class RealEstatePostController extends AbstractController<RealEstate> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String REQUEST_PARAM = "id";

	private PostType postType;

	@Inject
	private RealEstateFacade estateFacade;

	private RealEstate item;

	private User user;

	@PostConstruct
	public void init() {

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

	public void save() {
		try {
			if (getItem().getId() <= 0) {
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
	protected RealEstate getItem() {
		// TODO Auto-generated method stub
		return item;
	}

	@Override
	protected void setItem(RealEstate item) {
		// TODO Auto-generated method stub
		this.item = item;
	}

	@Override
	protected long getId() {
		// TODO Auto-generated method stub
		return item.getId();
	}

	@Override
	protected ICRUDOperations<RealEstate> getAbstractFacade() {
		// TODO Auto-generated method stub
		return estateFacade;
	}

	public PostType getPostType() {
		return postType;
	}

	public void setPostType(PostType postType) {
		this.postType = postType;
	}

}

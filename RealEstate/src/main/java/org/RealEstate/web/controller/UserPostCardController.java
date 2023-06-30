package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.controller.AbstractController;
import org.RealEstate.dto.ImageDto;
import org.RealEstate.enumerator.PostStatus;
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
import org.RealEstate.service.UploadImagesMultiPart;
import org.RealEstate.utils.Utility;
import org.omnifaces.util.Faces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.file.UploadedFiles;

@Named
@ViewScoped
public class UserPostCardController extends AbstractController<RealEstate> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String REQUEST_PARAM = "id";

	private PostType postType;
	private PropertyKindEnum kindEnum = PropertyKindEnum.REALESTATE;

	@Inject
	private ChaletFacade chaletFacade;
	@Inject
	private RealEstateFacade estateFacade;
	@Inject
	private VillageFacade villageFacade;

	private List<Village> villages = new ArrayList<Village>();

	private RealEstate item;
	private Chalet chalet = new Chalet();

	private User user;

	private TabView tabView;
	private int activeIndex = 0;

	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;
	private List<ImageDto> list = new ArrayList<>();
	private UploadedFiles files;

	@PostConstruct
	public void init() {
		villages = villageFacade.findAll();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		if (!facesContext.isPostback()) {
			String id = externalContext.getRequestParameterMap().get(REQUEST_PARAM);
			readTheFlashValue(id);

		}
	}

	public void handleFileUploadReal(FileUploadEvent event) {
		list.add(new ImageDto(event.getFile().getFileName(), event.getFile().getContent()));
		FacesMessage message = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	public void handleFileUploadChalet(FileUploadEvent event) {
		list.add(new ImageDto(event.getFile().getFileName(), event.getFile().getContent()));
		FacesMessage message = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<String> uploadToReal() {
		try {
			return uploadImagesMultiPart.uploadImagePostFrontEnd(list);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> uploadToChalet() {
		try {
			return uploadImagesMultiPart.uploadImagePostFrontEnd(list);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void readTheFlashValue(String id) {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();

		// ADD EDIT RealEstate
		if (flash.containsKey("new-realestate")) {
			kindEnum = PropertyKindEnum.REALESTATE;
			item = findRealWithId(id);
			if (item == null) {
				try {
					Faces.redirect("/error.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			postType = item.getPostType();
			Utility.addSuccessMessage("save_success");

			setActiveIndex(0);
		} else if (flash.containsKey("edit-realestate")) {
			kindEnum = PropertyKindEnum.REALESTATE;
			item = findRealWithId(id);
			if (item == null) {
				try {
					Faces.redirect("/error.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			postType = item.getPostType();
			setActiveIndex(0);
			// ADD EDIT Chalet
		} else if (flash.containsKey("new-chalet")) {
			kindEnum = PropertyKindEnum.CHALET;
			chalet = findChaletWithId(id);
			if (chalet == null) {
				try {
					Faces.redirect("/error.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			setActiveIndex(1);
			Utility.addSuccessMessage("save_success");

		} else if (flash.containsKey("edit-chalet")) {
			kindEnum = PropertyKindEnum.CHALET;
			chalet = findChaletWithId(id);
			if (chalet == null) {
				try {
					Faces.redirect("/error.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			setActiveIndex(1);
		}

	}

	public RealEstate findRealWithId(String id) {
		if (id != null && Long.parseLong(id) > 0) {
			return estateFacade.find(Long.parseLong(id));
		}
		return null;
	}

	public Chalet findChaletWithId(String id) {
		if (id != null && Long.parseLong(id) > 0) {
			return chaletFacade.find(Long.parseLong(id));
		}
		return null;
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
		if (kindEnum == PropertyKindEnum.REALESTATE) {
			saveRealEstate();
		} else if (kindEnum == PropertyKindEnum.CHALET) {
			saveChalet();
		} else {
			Utility.addErrorMessage("no such tab found to save");
		}
	}

	public void saveRealEstate() {
		try {
			if (getItem().getId() <= 0) {

				item.setPostDate(new Date());
				item.setUser(user);
				item.setPostStatus(PostStatus.PENDING);
				item.setPostType(postType);
				item.setImages(uploadToReal());
				super.save();
			} else {
				item.setPostStatus(PostStatus.PENDING);
				item = getAbstractFacade().save(item);
			}

			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.put("new-realestate", "true");
			changeUrl(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveChalet() {
		try {
			if (chalet.getId() <= 0) {
				chalet.setPostDate(new Date());
				chalet.setUser(user);
				chalet.setPostStatus(PostStatus.PENDING);
				chalet.setImages(uploadToChalet());
				chalet = chaletFacade.save(chalet);
			} else {
				chalet.setPostStatus(PostStatus.PENDING);
				chalet = chaletFacade.save(chalet);
			}
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.put("new-chalet", "true");
			changeUrl(chalet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean hasRoomsBathFloorElevator() {
		return (postType.equals(PostType.APPRATMENT_RENT) || postType.equals(PostType.APPRATMENT_SELL)
				|| postType.equals(PostType.OFFICE_RENT) || postType.equals(PostType.OFFICE_SELL));
	}

	public boolean hasPark() {
		return (postType.equals(PostType.APPRATMENT_RENT) || postType.equals(PostType.APPRATMENT_SELL)
				|| postType.equals(PostType.OFFICE_RENT) || postType.equals(PostType.OFFICE_SELL)
				|| postType.equals(PostType.SHOP_RENT) || postType.equals(PostType.SHOP_SELL));
	}

	public boolean hasGarden() {
		return (postType.equals(PostType.APPRATMENT_RENT) || postType.equals(PostType.APPRATMENT_SELL));
	}

	public boolean hasGreenBand() {
		return (postType.equals(PostType.OFFICE_SELL) || postType.equals(PostType.APPRATMENT_SELL)
				|| postType.equals(PostType.SHOP_SELL) || postType.equals(PostType.LAND));
	}

	public boolean hasBlockNo() {
		return (postType.equals(PostType.OFFICE_RENT) || postType.equals(PostType.OFFICE_SELL)
				|| postType.equals(PostType.APPRATMENT_SELL) || postType.equals(PostType.APPRATMENT_RENT)
				|| postType.equals(PostType.SHOP_RENT) || postType.equals(PostType.SHOP_SELL)
				|| postType.equals(PostType.LAND));
	}

	public boolean aShop() {
		return (postType.equals(PostType.SHOP_SELL) || postType.equals(PostType.SHOP_RENT));
	}

	public boolean aLand() {
		return (postType.equals(PostType.LAND));
	}

	private void changeUrl(Chalet chalet) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		try {
			Faces.redirect(url + "?" + REQUEST_PARAM + "=%s", chalet.getId() + "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void changeUrl(RealEstate estate) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		try {
			Faces.redirect(url + "?" + REQUEST_PARAM + "=%s", estate.getId() + "");

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

	public TabView getTabView() {
		return tabView;
	}

	public void setTabView(TabView tabView) {
		this.tabView = tabView;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public void setActiveTab(int index) {
		setActiveIndex(index);
		tabView.setActiveIndex(index);
	}

}

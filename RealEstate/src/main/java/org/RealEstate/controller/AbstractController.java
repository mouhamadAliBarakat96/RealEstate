package org.RealEstate.controller;

import java.io.Serializable;

import org.RealEstate.interfaces.ICRUDOperations;


public abstract class AbstractController<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected abstract T getItem();

	protected abstract void setItem(T item);

	protected abstract long getId();

	protected abstract ICRUDOperations<T> getAbstractFacade();

	
	public void remove() throws Exception {
		getAbstractFacade().remove(getItem());
	}

	public void save() throws Exception {
		setItem(getAbstractFacade().save(getItem()));
	}

	public T findById() {
		try {
			return getAbstractFacade().find(getId());
		} catch (Exception e) {
			return null;
		}
	}

	public T find(Object o) {
		try {
			return getAbstractFacade().find(o);
		} catch (Exception e) {
			return null;
		}
	}

}

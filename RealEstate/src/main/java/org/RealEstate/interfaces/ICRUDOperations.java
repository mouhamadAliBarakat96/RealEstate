package org.RealEstate.interfaces;


import java.util.List;

public interface ICRUDOperations<T> {

	public T save(T entity) throws Exception;

	public void remove(T entity) throws Exception;

	public void remove(T[] entityArray) throws Exception;

	public T find(Object id) throws Exception;

	public List<T> findAll();

	public List<T> findRange(int[] range);

	public int count();

	public T newEntity() throws InstantiationException, IllegalAccessException;
	
}

package org.RealEstate.dto;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

public class PaginationResponse<T> implements Serializable {

	@Expose
	private int page;

	@Expose
	private int size;

	@Expose
	private long totalCount;

	@Expose
	private List<T> data;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
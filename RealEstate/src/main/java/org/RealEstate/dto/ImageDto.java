package org.RealEstate.dto;

import java.io.Serializable;

public class ImageDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private byte[] content;

	public ImageDto(String name, byte[] content) {
		super();
		this.name = name;
		this.content = content;
	}

	public ImageDto() {
		
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}

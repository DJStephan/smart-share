package com.ftd.smartshare.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ViewDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class ViewReturnDto {
	
	@XmlElement(name = "message")
	private String message;
	
	public ViewReturnDto() {}
	
	public ViewReturnDto(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

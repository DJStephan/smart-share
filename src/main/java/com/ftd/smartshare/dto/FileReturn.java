package com.ftd.smartshare.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fileReturnDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileReturn {
	@XmlElement(name = "fileName")
	private String fileName;
	@XmlElement(name = "file")
	private byte[] file;
	@XmlElement(name = "returnMessage")
	private String returnMessage;
	public FileReturn() {}
	
	public FileReturn(String fileName, byte[] file) {
		this.fileName = "download" + fileName;
		this.file = file;
		if(fileName.equals("no file")) {
			this.returnMessage = "File could not be downloaded";
		}else {
			this.returnMessage = "file downloaded to " + this.fileName;
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	
	
	

}

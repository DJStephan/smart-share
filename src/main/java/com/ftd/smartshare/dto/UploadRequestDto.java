package com.ftd.smartshare.dto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "uploadRequestDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class UploadRequestDto {
	//@XmlElementWrapper(name = "fields")
    @XmlElement(name = "upload")
    private boolean upload = true;
    @XmlElement(name = "fileName")
    private String fileName;
    @XmlElement(name = "password")
    private String Password;
    @XmlElement(name = "expiration")
    private int expiration;
    @XmlElement(name = "maxDownloads")
    private int Maxdownloads;
    @XmlElement(name = "file")
    private byte[] file;
    
    
    
    public UploadRequestDto() {}
    
    public UploadRequestDto(String fileName, String password, int expiration, int maxDownloads, File file) {
    	this.fileName = fileName;
    	this.Password = password;
    	this.expiration = expiration;
    	this.Maxdownloads = maxDownloads;
    	try {
			this.file = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}

	public boolean isUpload() {
		return upload;
	}

	public void setUpload(boolean upload) {
		this.upload = upload;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public int getExpiration() {
		return expiration;
	}

	public void setExpiration(int experation) {
		this.expiration = experation;
	}

	public int getMaxdownloads() {
		return Maxdownloads;
	}

	public void setMaxdownloads(int maxdownloads) {
		Maxdownloads = maxdownloads;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}
	
	
}

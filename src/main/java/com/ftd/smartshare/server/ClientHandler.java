package com.ftd.smartshare.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;

import com.ftd.smartshare.client.commands.subcommands.Download;
import com.ftd.smartshare.client.commands.subcommands.Upload;
import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.UploadRequestDto;

import Dao.SmartShareDao;

public class ClientHandler implements Runnable{
	
	Socket client;
	
	public ClientHandler(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try(
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        		//DataOutputStream out  = new DataOutputStream(client.getOutputStream());//for booleans
    			InputStream bufferedInputStream = new BufferedInputStream(new DataInputStream(client.getInputStream()));
				)
		{
			StringReader stringReader = new StringReader(in.readLine());
			JAXBContext context = JAXBContext.newInstance(UploadRequestDto.class, DownloadRequestDto.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			//TODO will need if statement to determine if object id upload or download
			UploadRequestDto upload = (UploadRequestDto) unmarshaller.unmarshal(stringReader);
			
			//out.writeBoolean(true);
			//out.flush();
			//int bytesSize = bufferedInputStream.
			byte[] bytes = IOUtils.toByteArray(bufferedInputStream);
			SmartShareDao dao = new SmartShareDao();
			boolean saved = dao.saveFile(bytes, upload);
			System.out.println(saved);
			//bufferedInputStream.read(bytes);
//			for(int i = 0 ; i < bytes.length; i++) {
//				System.out.println(bytes[i]);
//			}
			
			
		}catch(IOException | JAXBException e) {
			e.printStackTrace();
		}
		
	}

}

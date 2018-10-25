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
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;

import com.ftd.smartshare.client.commands.subcommands.Download;
import com.ftd.smartshare.client.commands.subcommands.Upload;
import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.FileReturn;
import com.ftd.smartshare.dto.UploadRequestDto;
import com.ftd.smartshare.dto.ViewDto;
import com.ftd.smartshare.dto.ViewReturnDto;
import com.ftd.smartshare.utils.NoCloseInputStream;

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
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				)
		{
			
			String uploadDownload = in.readLine();
			System.out.println(uploadDownload);
			//StringReader stringReader = new StringReader(in.readLine());
			//in.close();
			System.out.println("stringreader");
			JAXBContext context = JAXBContext.newInstance(UploadRequestDto.class, DownloadRequestDto.class, FileReturn.class, ViewDto.class, ViewReturnDto.class);
			Marshaller marshaller = context.createMarshaller();
			Unmarshaller unmarshaller = context.createUnmarshaller();
			SmartShareDao dao = new SmartShareDao();
//			UploadRequestDto upload = (UploadRequestDto) unmarshaller.unmarshal(new NoCloseInputStream(client.getInputStream()));

			if(uploadDownload.equals("upload")) {
				UploadRequestDto upload = (UploadRequestDto) unmarshaller.unmarshal(new NoCloseInputStream(client.getInputStream()));
				System.out.println(uploadDownload);
//				SmartShareDao dao = new SmartShareDao();
				if(!(dao.fileInDatabase(upload.getFileName()))) {
					System.out.println("file saving");
					if(dao.saveFile(upload)) {
						System.out.println(client.isInputShutdown());
						out.write("file saved successfully!");
						out.newLine();
						out.flush();
					}else {
						out.write("file did not save :(");
						out.newLine();
						out.flush();
					}
				}else {
					System.out.println("about to send something to the client");
					out.write("file name already exists in database");
					out.newLine();
					out.flush();
				}

			}else if(uploadDownload.equals("download")) {
				DownloadRequestDto download = (DownloadRequestDto) unmarshaller.unmarshal(new NoCloseInputStream(client.getInputStream()));
				byte[] returnFile = dao.getFile(download);
				if(returnFile.length == 0) {
					
					//Marshaller marshaller = context.createMarshaller();
					System.out.println("sending downloaded file");
					marshaller.marshal(new FileReturn("no file", returnFile), client.getOutputStream());
//					out.write("file could not be downloaded");
//					out.newLine();
//					out.flush();
				}else {
					//Marshaller marshaller = context.createMarshaller();
					System.out.println("sending downloaded file");
					marshaller.marshal(new FileReturn(download.getFileName(), returnFile), client.getOutputStream());
					//client.close();
					
//					out.write("file downloaded successsfully to .../" + download.getFileName());
//					out.newLine();
//					out.flush();
				}
			}else if(uploadDownload.equals("view")) {
				ViewDto view = (ViewDto) unmarshaller.unmarshal(client.getInputStream());
				String message = dao.View(view.getFileName(), view.getPassword());
				ViewReturnDto vDto = new ViewReturnDto(message);
				marshaller.marshal(vDto, client.getOutputStream());
				
				
				

			}
			
			
			
		}catch(IOException | JAXBException e) {
			e.printStackTrace();
		}
		
	}

}

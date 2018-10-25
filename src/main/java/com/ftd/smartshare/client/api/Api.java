package com.ftd.smartshare.client.api;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.file.Files;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.ftd.smartshare.client.commands.subcommands.Upload;
import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.UploadRequestDto;
import com.ftd.smartshare.utils.NotImplementedException;

public final class Api {

    private static final String HOST    = "localhost";
    private static final int    PORT    = 3000;

    private Api () {
        throw new UnsupportedOperationException();
    }

    /**
     * Send download request
     *
     * @param downloadRequestDto    JAXB annotated class representing the download request
     * @return true if request was successful and false if unsuccessful
     */
    public static boolean download(DownloadRequestDto downloadRequestDto) {
//        throw new NotImplementedException();
    	return false;
        
    }

    /**
     * Send upload request
     *
     * @param uploadRequestDto      JAXB annotated class representing the upload request
     * @return true if request was successful and false if unsuccessful
     */
    public static boolean upload(UploadRequestDto uploadRequestDto, File file) {
        //throw new NotImplementedException();
    	try(
    			Socket socket = new Socket(HOST, PORT);
        		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//        		DataInputStream in  = new DataInputStream(socket.getInputStream());//for booleans
    			InputStream fileInputStream = new FileInputStream(file);
    			OutputStream bufferedOutputStream = new BufferedOutputStream(new DataOutputStream(socket.getOutputStream()));

    			){
//    		Socket socket = new Socket(HOST, PORT);
    		JAXBContext context = JAXBContext.newInstance(UploadRequestDto.class);
    		Marshaller marshaller = context.createMarshaller();
    		StringWriter stringWriter = new StringWriter();
    		marshaller.marshal(uploadRequestDto, stringWriter);
//    		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    		out.write(stringWriter.toString());
    		out.newLine();
    		out.flush();
    		
//    		DataInputStream in  = new DataInputStream(socket.getInputStream());
//    		try(DataInputStream in  = new DataInputStream(socket.getInputStream());){
//        		uniqueFile = in.readBoolean();//determine if file is in DB. true if not in DB, false if in DB
//    		}catch(EOFException eof) {
    		
			//InputStream fileInputStream = new FileInputStream(file);
			byte[] bytes = Files.readAllBytes(file.toPath());
			//fileInputStream.read(bytes);
			//bufferedOutputStream.write(bytes.length);
			bufferedOutputStream.write(bytes);
			bufferedOutputStream.flush();
		
    	}
        
        catch(IOException | JAXBException e) {
        	e.printStackTrace();
        }
    	return false;
    }

}

package com.ftd.smartshare.client.api;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.file.Files;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;

import com.ftd.smartshare.client.commands.subcommands.Upload;
import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.FileReturn;
import com.ftd.smartshare.dto.UploadRequestDto;
import com.ftd.smartshare.dto.ViewDto;
import com.ftd.smartshare.dto.ViewReturnDto;
import com.ftd.smartshare.utils.NoCloseInputStream;
import com.ftd.smartshare.utils.NotImplementedException;

public final class Api {

	private static final String HOST = "localhost";
	private static final int PORT = 3000;
	private static final Object ViewDto = null;

	private Api() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Send download request
	 *
	 * @param downloadRequestDto JAXB annotated class representing the download
	 *                           request
	 * @return true if request was successful and false if unsuccessful
	 */
	public static String download(DownloadRequestDto downloadRequestDto) {
//        throw new NotImplementedException();
		String returnMessage = null;

		try (Socket socket = new Socket(HOST, PORT);
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				OutputStream bufferedOutputStream = new BufferedOutputStream(
						new DataOutputStream(socket.getOutputStream()));
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		) {
			JAXBContext context = JAXBContext.newInstance(DownloadRequestDto.class, FileReturn.class);
			Marshaller marshaller = context.createMarshaller();
			out.write("download");
			out.newLine();
			out.flush();
			marshaller.marshal(downloadRequestDto, socket.getOutputStream());
			socket.shutdownOutput();
			Unmarshaller unmarshaller = context.createUnmarshaller();
			System.out.println("getting downloaded file");

			FileReturn downloadedFile = (FileReturn) unmarshaller.unmarshal(socket.getInputStream());
			if(!(downloadedFile.getFile().length == 0)) {
				File fileDl = new File(downloadedFile.getFileName());
				fileDl.createNewFile();
				FileUtils.writeByteArrayToFile(fileDl, downloadedFile.getFile());

			}
//			FileUtils.writeByteArrayToFile(new File(".../" + downloadedFile.getFileName()), downloadedFile.getFile());
			returnMessage = downloadedFile.getReturnMessage();

		}

		catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
		return returnMessage;
	

	}

	/**
	 * Send upload request
	 *
	 * @param uploadRequestDto JAXB annotated class representing the upload request
	 * @return true if request was successful and false if unsuccessful
	 */
	public static String upload(UploadRequestDto uploadRequestDto) {
		// throw new NotImplementedException();
		String returnMessage = null;

		try (Socket socket = new Socket(HOST, PORT);
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				OutputStream bufferedOutputStream = new BufferedOutputStream(
						new DataOutputStream(socket.getOutputStream()));
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		) {
			JAXBContext context = JAXBContext.newInstance(UploadRequestDto.class);
			Marshaller marshaller = context.createMarshaller();
			out.write("upload");
			out.newLine();
			out.flush();
			marshaller.marshal(uploadRequestDto, socket.getOutputStream());
			socket.shutdownOutput();
			System.out.println("Sent something to the server");
			returnMessage = in.readLine();

		}

		catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
		return returnMessage;
	}
	
	public static String View(ViewDto viewDto) {
		String returnMessage = null;
		try (Socket socket = new Socket(HOST, PORT);
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				OutputStream bufferedOutputStream = new BufferedOutputStream(
						new DataOutputStream(socket.getOutputStream()));
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		) {
			JAXBContext context = JAXBContext.newInstance(ViewDto.class);
			Marshaller marshaller = context.createMarshaller();
			out.write("view");
			out.newLine();
			out.flush();
			marshaller.marshal(ViewDto, socket.getOutputStream());
			socket.shutdownOutput();
			Unmarshaller unmarshaller = context.createUnmarshaller();
			ViewReturnDto vDto = (ViewReturnDto) unmarshaller.unmarshal(socket.getInputStream());
			returnMessage = vDto.getMessage();

		}

		catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
		return returnMessage;
	}

}

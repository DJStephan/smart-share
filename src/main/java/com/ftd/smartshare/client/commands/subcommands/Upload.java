package com.ftd.smartshare.client.commands.subcommands;

import com.ftd.smartshare.client.api.Api;
import java.sql.Timestamp;
import com.ftd.smartshare.dto.UploadRequestDto;
import com.ftd.smartshare.utils.PasswordGenerator;
import org.apache.commons.text.RandomStringGenerator;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(
        description = "Uploads file using a given 'password', expiration (60 minutes by default), a max downloads (1 by default)",
        name = "upload",
        aliases = "u",
        mixinStandardHelpOptions = true
)
public class Upload implements Runnable {

    @CommandLine.Parameters(arity="1", index = "0", description = "The file to be uploaded")
    private File file;

//    @CommandLine.Parameters(arity="0", index = "1", description = "The password for the file")
//    private String password = PasswordGenerator.generate();
    
    @CommandLine.Option(names = {"-p", "--password"}, description = "password to access file")
    private String password = PasswordGenerator.generate();
    
    @CommandLine.Option(names = {"-ex", "--expiration"}, description = "time in minutes to make file available -- defaults to 60 minutes")
    private int expiration = 60;
    
    @CommandLine.Option(names = {"-max", "--maxDownloads"}, description = "number of times file can be downloaded -- defaults to 5")
    private int maxDownloads = 5;
    
    private String returnMessage;

    public void run() {
    	String returnMessage = Api.upload(new UploadRequestDto(file.getName(), password, expiration, maxDownloads, this.file ));
        System.out.println("Uploading: " + file.getAbsolutePath());
        System.out.println("Password will be printed below");
        System.out.println(password);
        System.out.println(returnMessage);


       
    }

	public File getFile() {
		return file;
	}

	public String getPassword() {
		return password;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	
	


}
	

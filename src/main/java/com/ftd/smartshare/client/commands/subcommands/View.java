package com.ftd.smartshare.client.commands.subcommands;

import com.ftd.smartshare.client.api.Api;
import com.ftd.smartshare.dto.ViewDto;

import picocli.CommandLine;

public class View implements Runnable{
	
	@CommandLine.Parameters(arity = "1", index = "0", description = "name of file to veiw")
	private String fileName;
	
	@CommandLine.Parameters(arity = "1", index = "1", description = "file password")
	private String password;
	
	

	@Override
	public void run() {
		System.out.println(Api.View(new ViewDto(fileName, password)));
		
	}

}

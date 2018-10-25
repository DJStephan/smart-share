package com.ftd.smartshare.client;

import com.ftd.smartshare.client.commands.SmartShare;

import picocli.CommandLine;

class Main {

	public static void main(String[] args) {
		// CommandLine.run(new SmartShare()); // Pass cli arguments here
//        CommandLine.run(new SmartShare(), "-h");
//        CommandLine.run(new SmartShare(), "--version");
//        CommandLine.run(new SmartShare(), "upload");
//        CommandLine.run(new SmartShare(), "upload", "test.txt");
//        CommandLine.run(new SmartShare(), "upload", ".gitignore");
		CommandLine.run(new SmartShare(), "upload", "test.txt", "-p password");
//    	CommandLine.run(new SmartShare(), "download", "test.txt", "ahzcrqyadhtpqqchnooj");
	}

}

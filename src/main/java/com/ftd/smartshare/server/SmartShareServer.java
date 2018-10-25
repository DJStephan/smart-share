package com.ftd.smartshare.server;

import java.io.IOException;
import java.net.ServerSocket;

public class SmartShareServer {
    public static void main(String[] args) {
		try(ServerSocket server = new ServerSocket(3000);){
			while(true) {
				new Thread(new ClientHandler(server.accept())).start();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}

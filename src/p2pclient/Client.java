package p2pclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import p2pserver.P2PServer;

public class Client {
	
	private static final String SERVER_ADDRESS = "127.0.0.1";
	private static final int PORT_NUMBER = 7734;
	
	public static void main(String args[]) throws Exception {
		Socket client_connection = new Socket(SERVER_ADDRESS, PORT_NUMBER); //host name & port number
		BufferedReader input = new BufferedReader(new InputStreamReader(client_connection.getInputStream()));
		PrintWriter output = new PrintWriter(client_connection.getOutputStream(), true); 
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter Upload port of client");
		ServerSocket clientUploadServer = new ServerSocket(Integer.valueOf(br.readLine()));
		Runnable p2p_client = new P2PClient(clientUploadServer);
		Thread thread = new Thread(p2p_client);
		thread.start();
	}
}

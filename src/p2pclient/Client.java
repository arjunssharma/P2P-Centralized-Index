package p2pclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

	private static final String SERVER_ADDRESS = "127.0.0.1";
	private static final int PORT_NUMBER = 7734;

	public static void main(String args[]) throws Exception {
		Socket client_connection = new Socket(SERVER_ADDRESS, PORT_NUMBER); // host name & port number
		BufferedReader input_from_server = new BufferedReader(new InputStreamReader(client_connection.getInputStream()));
		PrintWriter output_to_server = new PrintWriter(client_connection.getOutputStream(), true);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter host name of client");
		String host_name = br.readLine();
		System.out.println("Enter Upload port of client");
		Integer uploadPort = Integer.valueOf(br.readLine());
		ServerSocket clientUploadServer = new ServerSocket(uploadPort);
		Runnable p2p_client = new P2PClient(clientUploadServer);
		Thread thread = new Thread(p2p_client);
		thread.start();

		output_to_server.println(host_name);
		output_to_server.println(uploadPort);
		System.out.println(input_from_server.readLine()); // $host_name added to the active list
		System.out.println(input_from_server.readLine()); // ENTER ADD, LIST ALL, LOOKUP, GET or END requests
		while(true) {
			String line = br.readLine();
			if(line.startsWith("ADD")) {
				output_to_server.println(line);
				output_to_server.println(br.readLine());
				output_to_server.println(br.readLine());
				output_to_server.println(br.readLine());
				String s;
				while((s = input_from_server.readLine()) != null)
					System.out.println(s);
			}
			else
				break;
		}
		
		
		if(client_connection != null)
			client_connection.close();
	}
}

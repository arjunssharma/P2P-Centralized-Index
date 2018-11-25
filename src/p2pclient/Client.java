package p2pclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client extends P2PClientAbstract {

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
			output_to_server.flush();
			String line = br.readLine();
			//System.out.println("LINE IS : " + line);
			if(line.startsWith("ADD")) {
				output_to_server.println(line);
				output_to_server.println(br.readLine());
				output_to_server.println(br.readLine());
				output_to_server.println(br.readLine());
				String s;
				while(!(s = input_from_server.readLine()).equals("EOF"))
					System.out.println(s);
			}
			else if(line.startsWith("LIST ALL") && line.split(" ").length == 3) {
				String message = line.split(" ")[2];
				if(!message.equals(VERSION)) {
					System.out.println(responseCode(-1));
					continue;
				}
				
				output_to_server.println(line);
				Integer count = Integer.valueOf(input_from_server.readLine());
				//System.out.println(count);
				for(int i = 0; i < count; i++) {
					System.out.println(input_from_server.readLine());
				}
			}
			else if(line.startsWith("LOOKUP")) {
				
			}
			else if(line.startsWith("GET")) {
				
			}
			else if(line.startsWith("END")) {
				break;
			}
			else {
				System.out.println("Invalid Command! Try Again.");
			}
			
			System.out.println("ENTER ADD, LIST ALL, LOOKUP, GET or END requests");
		}
		
		
		if(client_connection != null)
			client_connection.close();
	}
}

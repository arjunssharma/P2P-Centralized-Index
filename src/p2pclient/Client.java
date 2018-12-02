package p2pclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client extends P2PClientAbstract {

	private static String SERVER_ADDRESS;
	private static final int PORT_NUMBER = 7734;
	//private static Map<String, Integer> hostToPortMap = new HashMap<>();
	//private static Map<Integer, String> rfcNumberToTitleMap = new HashMap<>();
	
	public static void main(String args[]) throws Exception {
		//boolean exists = new File(System.getProperty("user.dir") + "/" + "one.ncsu.edu" + "/" + "123.txt").exists();
		//System.out.println(exists);
		//System.out.println(System.getProperty("user.dir"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter Server Address");
		SERVER_ADDRESS = br.readLine();
		Socket client_server_connection = new Socket(SERVER_ADDRESS, PORT_NUMBER); // host name & port number
		BufferedReader input_from_server = new BufferedReader(new InputStreamReader(client_server_connection.getInputStream()));
		PrintWriter output_to_server = new PrintWriter(client_server_connection.getOutputStream(), true);
		
		System.out.println("Enter host name of client");
		String host_name = br.readLine();
		System.out.println("Enter Upload port of client");
		Integer uploadPort = Integer.valueOf(br.readLine());
		ServerSocket clientUploadServer = new ServerSocket(uploadPort);
		Runnable p2p_client = new P2PClient(clientUploadServer, host_name);
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
				List<String> message = new ArrayList<>();
				message.add(line); // ADD RFC 123 P2P-CI/1.0
				message.add(br.readLine()); // Host: thishost.csc.ncsu.edu
				message.add(br.readLine()); // Port: 5678
				message.add(br.readLine()); // Title: A Proferred Official ICP
				//Integer rfcNumber = Integer.valueOf(message.get(0).split(" ")[2]);
				//String title = message.get(3).split(": ")[1];
				output_to_server.println(message.get(0));
				output_to_server.println(message.get(1));
				output_to_server.println(message.get(2));
				output_to_server.println(message.get(3));
				String s;
				while (!(s = input_from_server.readLine()).equals(EOF))
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
				if(count > 0) {
					for(int i = 0; i < count; i++) {
						System.out.println(input_from_server.readLine());
					}
				}
				else
					System.out.println("No Entries Found");
			}
			else if(line.startsWith("LOOKUP")) {
				output_to_server.println(line);
				output_to_server.println(br.readLine());
				output_to_server.println(br.readLine());
				String s;
				while (!(s = input_from_server.readLine()).equals(EOF))
					System.out.println(s);
			}
			else if (line.startsWith("GET")) {
				List<String> message = new ArrayList<>();
				message.add(line); // GET RFC 1234 P2P-CI/1.0
				message.add(br.readLine()); // Host: somehost.csc.ncsu.edu
				message.add(br.readLine()); // OS: Mac OS 10.4.1
				if(!message.get(0).split(" ")[3].equals(VERSION)) {
					System.out.println(responseCode(-1));
					continue;
				}
				
				System.out.println("Enter Port Number");
				Integer portify = Integer.valueOf(br.readLine());
				Integer RFCNumber = Integer.valueOf(message.get(0).split(" ")[2]);
				Socket clientToclientRequest = new Socket(message.get(1).split(" ")[1], portify);
				BufferedReader input_from_client = new BufferedReader(new InputStreamReader(clientToclientRequest.getInputStream()));
				PrintWriter output_to_client = new PrintWriter(clientToclientRequest.getOutputStream(), true);				
				System.out.println(input_from_client.readLine());
				
				String fileName = RFCNumber + ".txt"; //123.txt
				output_to_client.println(fileName);
						
				File dir = new File(System.getProperty("user.dir") + "/" + host_name);
					if (!dir.exists()) 
						dir.mkdirs();
				
				File output_file = new File(System.getProperty("user.dir") + "/" + host_name + "/" + fileName);
	            PrintWriter pw = new PrintWriter(output_file);
				String s;
				while (!(s = input_from_client.readLine()).equals(EOF))
					pw.println(s);
				
				if(pw != null)
					pw.close();
				
				System.out.println("File downloaded in directory: " + System.getProperty("user.dir") + "/" + host_name + "/" + fileName);
				clientToclientRequest.close();
			}
			else if(line.startsWith("END")) {
				output_to_server.println(line);
				output_to_server.println(host_name);
				String s;
				while (!(s = input_from_server.readLine()).equals(EOF))
					System.out.println(s);
				
				if(client_server_connection != null)
					client_server_connection.close();
				System.exit(1);
			}
			else {
				System.out.println("Invalid Command! Try Again.");
			}
			//System.out.println("ENTER ADD, LIST ALL, LOOKUP, GET or END requests");
		}
	}
}

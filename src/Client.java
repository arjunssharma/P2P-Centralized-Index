import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
*
* @author Arjun Sharma
*
*/

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
				System.out.println("-----------------------------------------------------------------------------------------------------");
				while (!(s = input_from_server.readLine()).equals(EOF))
					System.out.println(s);
				System.out.println("-----------------------------------------------------------------------------------------------------");
			}
			else if(line.startsWith("LIST ALL") && line.split(" ").length == 3) {
				String message = line.split(" ")[2];
				System.out.println("-----------------------------------------------------------------------------------------------------");
				if(!message.equals(VERSION)) {
					System.out.println(responseCode(-1));
					continue;
				}
				
				System.out.println(responseCode(1));
				output_to_server.println(line);
				Integer count = Integer.valueOf(input_from_server.readLine());
				if(count > 0) {
					for(int i = 0; i < count; i++) {
						System.out.println(input_from_server.readLine());
					}
				}
				else
					System.out.println("No Entries Found");
				System.out.println("-----------------------------------------------------------------------------------------------------");
			}
			else if(line.startsWith("LOOKUP")) {
				output_to_server.println(line); //LOOKUP RFC 123 P2P-CI/1.0
				output_to_server.println(br.readLine()); //Host: 152.46.17.34
				output_to_server.println(br.readLine()); //Title: A Proferred Official ICP
				System.out.println("-----------------------------------------------------------------------------------------------------");
				if(!line.split(" ")[3].equals(VERSION)) {
					System.out.println(responseCode(-1));
					continue;
				}
				
				System.out.println(responseCode(1));
				String s;
				while (!(s = input_from_server.readLine()).equals(EOF))
					System.out.println(s);
				System.out.println("-----------------------------------------------------------------------------------------------------");
			}
			else if (line.startsWith("GET")) {
				List<String> message = new ArrayList<>();
				message.add(line); // GET RFC 1234 P2P-CI/1.0
				message.add(br.readLine()); // Host: somehost.csc.ncsu.edu
				message.add(br.readLine()); // OS: Mac OS 10.4.1
				message.add(br.readLine()); //Title: A Proferred Official ICP
				System.out.println("-----------------------------------------------------------------------------------------------------");
				if(!message.get(0).split(" ")[3].equals(VERSION)) {
					System.out.println(responseCode(-1));
					continue;
				}
				
				System.out.println(responseCode(1));
				Integer RFCNumber = Integer.valueOf(message.get(0).split(" ")[2]);
				output_to_server.println(line);
				//System.out.println("Enter Port Number of Client with RFC from LOOK UP request");
				output_to_server.println(message.get(1));
				output_to_server.println(RFCNumber);
				String portify = input_from_server.readLine();
				if(portify == null || portify.equals("")) {
					System.out.println("Port number not found in RFC list");
					continue;
				}
				else {
					Socket clientToclientRequest = new Socket(message.get(1).split(" ")[1], Integer.valueOf(portify));
					BufferedReader input_from_client = new BufferedReader(new InputStreamReader(clientToclientRequest.getInputStream()));
					PrintWriter output_to_client = new PrintWriter(clientToclientRequest.getOutputStream(), true);				
					//System.out.println(input_from_client.readLine());
					
					String fileName = RFCNumber + ".txt"; //123.txt
					output_to_client.println(fileName);
							
					File dir = new File(System.getProperty("user.dir") + "/" + host_name);
						if (!dir.exists()) 
							dir.mkdirs();
					
					File output_file = new File(System.getProperty("user.dir") + "/" + host_name + "/" + fileName);
		            PrintWriter pw = new PrintWriter(output_file);
		            System.out.println(input_from_client.readLine());
		            System.out.println(input_from_client.readLine());
		            System.out.println(input_from_client.readLine());
		            System.out.println(input_from_client.readLine());
		            
		            String s;
					while (!(s = input_from_client.readLine()).equals(EOF))
						pw.println(s);
					
					if(pw != null)
						pw.close();
					
					System.out.println("File downloaded in directory: " + System.getProperty("user.dir") + "/" + host_name + "/" + fileName);
					System.out.println("-----------------------------------------------------------------------------------------------------");
					//Add this RFC to this client
					String first_line = message.get(0).replace("GET", "ADD");
					String second_line = new String("Host: " + host_name);
					String third_line = new String("Port: " + uploadPort);
					String forth_line = message.get(3);
					message = new ArrayList<>();
					message.add(first_line); // ADD RFC 1234 P2P-CI/1.0
					message.add(second_line); // Host: thishost.csc.ncsu.edu
					message.add(third_line); // Port: 5678
					message.add(forth_line); // Title: A Proferred Official ICP
					
					output_to_server.println(message.get(0));
					output_to_server.println(message.get(1));
					output_to_server.println(message.get(2));
					output_to_server.println(message.get(3));
					String t;
					while (!(t = input_from_server.readLine()).equals(EOF))
						System.out.println(t);
					System.out.println("-----------------------------------------------------------------------------------------------------");
					clientToclientRequest.close();
				}
			}
			else if(line.startsWith("END")) {
				output_to_server.println(line);
				output_to_server.println(host_name);
				String s;
				System.out.println("-----------------------------------------------------------------------------------------------------");
				while (!(s = input_from_server.readLine()).equals(EOF))
					System.out.println(s);
				System.out.println("-----------------------------------------------------------------------------------------------------");
				if(client_server_connection != null)
					client_server_connection.close();
				System.exit(1);
			}
			else {
				System.out.println("-----------------------------------------------------------------------------------------------------");
				System.out.println("Invalid Command! Try Again.");
				System.out.println("-----------------------------------------------------------------------------------------------------");
			}
			//System.out.println("ENTER ADD, LIST ALL, LOOKUP, GET or END requests");
		}
	}
}

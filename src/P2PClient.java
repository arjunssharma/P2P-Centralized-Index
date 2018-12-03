import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
*
* @author Arjun Sharma
*
*/

public class P2PClient extends P2PClientAbstract implements Runnable {
	
	public P2PClient(ServerSocket clientUploadServer, String clientName) {
		super();
		this.clientName = clientName;
		this.clientUploadServer = clientUploadServer;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Socket another_client_request = null;
		
		while(true) {
			try {
				another_client_request = clientUploadServer.accept();
				BufferedReader input_from_client = new BufferedReader(new InputStreamReader(another_client_request.getInputStream()));
				PrintWriter output_to_client = new PrintWriter(another_client_request.getOutputStream(), true); 
				//output_to_client.println("Connection made with client");
				String fileName = input_from_client.readLine();
				
				File dir = new File(System.getProperty("user.dir") + "/" + this.clientName);
				if (!dir.exists()) 
					dir.mkdirs();
				
				boolean exists = new File(System.getProperty("user.dir") + "/" + this.clientName + "/" + fileName).exists();
				if(!exists) {
					output_to_client.println("Request RFC file doesn't exist");
					output_to_client.println(EOF);
					continue;
				}
				
				File file = new File(System.getProperty("user.dir") + "/" + clientName + "/" + fileName);
				output_to_client.println("Date: "+ (new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")).format(new Date(0)) + " GMT");
				output_to_client.println("OS: " + System.getProperty("os.name"));
				output_to_client.println("Last Modified: " + (new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")).format(new Date(file.lastModified())) +" GMT");
				output_to_client.println("Content-Length: " + file.length() + "\nContent-Type: text/text \n");
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				while((line = br.readLine()) != null) {
					output_to_client.println(line);
				}
				
				output_to_client.println(EOF);
				if(br != null)
					br.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(another_client_request != null)
					try {
						another_client_request.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		
		
	}

}

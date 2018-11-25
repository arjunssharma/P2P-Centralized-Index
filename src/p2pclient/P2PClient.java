package p2pclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class P2PClient extends P2PClientAbstract implements Runnable {

	public P2PClient(ServerSocket clientUploadServer) {
		super();
		this.clientUploadServer = clientUploadServer;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Socket client = null;
		
		try {
			client = clientUploadServer.accept();
			System.out.println("Client to Client connection successful");
			BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter output = new PrintWriter(client.getOutputStream(), true); 
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(client != null)
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}

}

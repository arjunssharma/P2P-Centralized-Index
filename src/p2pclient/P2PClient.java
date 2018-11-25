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
		Socket another_client_request = null;
		
		try {
			another_client_request = clientUploadServer.accept();
			BufferedReader input_from_client = new BufferedReader(new InputStreamReader(another_client_request.getInputStream()));
			PrintWriter output_to_client = new PrintWriter(another_client_request.getOutputStream(), true); 
			System.out.println("Client to Client connection successful");
			
			while(true) {
				
			}
			//GET RFC 1234 P2P-CI/1.0
			//Host: somehost.csc.ncsu.edu
			//OS: Mac OS 10.4.1
			
			
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

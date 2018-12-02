package p2pserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

	private static List<ActivePeer> activePeerList = Collections.synchronizedList(new ArrayList<ActivePeer>());
	private static List<RFCIndex> rfcIndexList = Collections.synchronizedList(new ArrayList<RFCIndex>());
	private static final int PORT_NUMBER = 7734; //as mentioned in the project 1 doc
	
	public static void main(String args[]) throws Exception {		
		ServerSocket accept_socket =  new ServerSocket(PORT_NUMBER);
		try {
			while(true) {
				Socket sock = accept_socket.accept();
				Runnable p2p_server = new P2PServer(activePeerList, rfcIndexList, sock);
				Thread thread = new Thread(p2p_server);
				thread.start();
			}
		}
		catch(Exception ex) {
			throw new Exception(ex.getMessage());
		}
		finally {
			accept_socket.close();
		}
	}
}

package p2pserver;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pojo.ActivePeer;
import pojo.RFCIndex;

public class P2PServer implements Runnable, Server {

	public static List<ActivePeer> activePeerList = Collections.synchronizedList(new ArrayList<ActivePeer>());
	public static List<RFCIndex> rfcIndexList = Collections.synchronizedList(new ArrayList<RFCIndex>());
	public Socket socket = null;

	
	public static void main(String args[]) throws Exception {
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}

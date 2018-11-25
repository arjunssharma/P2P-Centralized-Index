package p2pserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import pojo.ActivePeer;
import pojo.RFCIndex;

public class P2PServer extends P2PAbstract implements Runnable {

	public P2PServer(List<ActivePeer> activePeerList, List<RFCIndex> rfcIndexList, Socket sock) {
		super();
		this.activePeerList = activePeerList;
		this.rfcIndexList = rfcIndexList;
		this.socket = sock;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

			String line;
			List<String> message = new ArrayList<>();
			output.println("Enter host name and port number");
			message.add(input.readLine());
			message.add(input.readLine());

			// provide host name and port number of client
			String hostName = message.get(0);
			int portNumber = Integer.valueOf(message.get(1));
			ActivePeer peer = new ActivePeer(hostName, portNumber);
			activePeerList.add(peer);
			output.println(peer.getHostName() + " added to the active list");
			output.println("Enter ADD, LIST ALL, LOOKUP or END");
			while (true) {
				message = new ArrayList<>();
				line = input.readLine();
				if (line.startsWith("ADD")) {
					message.add(line); // ADD RFC 123 P2P-CI/1.0
					message.add(input.readLine()); // Host: thishost.csc.ncsu.edu
					message.add(input.readLine()); // Port: 5678
					message.add(input.readLine()); // Title: A Proferred Official ICP
					int code = addRFC(message, output);
					if (code != 1)
						responseCode(output, code);
				} 
				else if (line.startsWith("LIST ALL")) {
					message.add(line); // LIST ALL P2P-CI/1.0
					message.add(input.readLine()); // Host: thishost.csc.ncsu.edu
					message.add(input.readLine()); // Port: 5678
					if(!message.get(0).equals(VERSION)) {
						responseCode(output, -1);
						break;
					}
					
					for (RFCIndex rfc : rfcIndexList) {
						output.println(rfc.getRFCNumber() + " " + rfc.getTitle() + " " + rfc.getRFCHostName());
					}
				} 
				else if (line.startsWith("LOOKUP")) {
					int code = -2; // not found
					Integer port = null;
					StringBuilder lookUpList = new StringBuilder();
					message.add(line); // LOOKUP RFC 3457 P2P-CI/1.0
					message.add(input.readLine()); // Host: thishost.csc.ncsu.edu
					message.add(input.readLine()); // Port: 5678
					message.add(input.readLine()); // Title: Requirements for IPsec Remote Access Scenarios
					Integer clientRequestRFCNumber = Integer.valueOf(message.get(0).split(" ")[2]);
					String clientRequestTitle = message.get(3).split(": ")[1];
					for (RFCIndex rfc : rfcIndexList) {
						if (rfc.getRFCNumber().equals(clientRequestRFCNumber) || rfc.getTitle().equals(clientRequestTitle)) {
							for (ActivePeer ap : activePeerList) {
								if (ap.getHostName().equals(rfc.getRFCHostName())) {
									port = ap.getIndex();
								}
							}

							lookUpList.append("Host: " + rfc.getRFCHostName() + "\nPort: " + String.valueOf(port));
							code = 1;
						}
					}

					if (code == 1) {
						output.println(lookUpList.toString());
					} else {
						responseCode(output, code);
					}
				} else if (line.startsWith("END")) {
					socket.close();
					break;
				}
				else {
					output.println("Invalid Request");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// ADD RFC 123 P2P-CI/1.0
	// Host: thishost.csc.ncsu.edu
	// Port: 5678
	// Title: A Proferred Official ICP
	private int addRFC(List<String> message, PrintWriter output) {
		Integer RFCNumber = null;
		Integer port = null;
		String title = null;
		String RFCHostName = null;
		for (int i = 0; i < message.size(); i++) {
			String line = message.get(i);
			if (line.startsWith("ADD")) {
				String split[] = line.split(" ");
				try {
					if (split[1].equals("RFC")) {
						RFCNumber = Integer.parseInt(split[2]);
					} else if (!split[1].equals("RFC")) {
						return 0;
					} else if (!split[3].equals(VERSION)) {
						return -1;
					}
				} catch (Exception ex) {
					return 0;
				}
			} else if (line.startsWith("Host")) {
				RFCHostName = line.split(" ")[1];
			} else if (line.startsWith("Port")) {
				try {
					port = Integer.parseInt(line.split(" ")[1]);
				} catch (Exception ex) {
					return 0;
				}
			} else if (line.startsWith("Title")) {
				title = line.split(": ")[1];
			} else {
				return 0; // bad request
			}
		}

		RFCIndex rfc = new RFCIndex(RFCNumber, title, RFCHostName);
		rfcIndexList.add(rfc);
		responseCode(output, 1);
		output.println("RFC " + RFCNumber + " " + title + " " + RFCHostName + " " + port);

		return 1;
	}
}

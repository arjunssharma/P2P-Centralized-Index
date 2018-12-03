import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
*
* @author Arjun Sharma
*
*/

public class P2PServer extends P2PServerAbstract implements Runnable {

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
			//output.println("Enter host name and port number");
			message.add(input.readLine());
			message.add(input.readLine());

			// provide host name and port number of client
			String hostName = message.get(0);
			int portNumber = Integer.valueOf(message.get(1));
			ActivePeer peer = new ActivePeer(hostName, portNumber);
			activePeerList.add(peer);
			output.println(peer.getHostName() + " added to the active list");
			output.println("ENTER ADD, LIST ALL, LOOKUP, GET or END requests");
			while (true) {
				message = new ArrayList<>();
				line = input.readLine();
				if (line.startsWith("ADD")) {
					message.add(line); // ADD RFC 123 P2P-CI/1.0
					message.add(input.readLine()); // Host: thishost.csc.ncsu.edu
					message.add(input.readLine()); // Port: 5678
					message.add(input.readLine()); // Title: A Proferred Official ICP
					
					System.out.println("------------------- Add request from peer -------------------");
					for(int i = 0; i < message.size(); i++)
						System.out.println(message.get(i));
					
					int code = addRFC(message, output);
					if (code != 1)
						output.println(responseCode(code));
					output.println(EOF);
				} 
				else if (line.startsWith("LIST ALL")) {
					message.add(line); // LIST ALL P2P-CI/1.0
					//message.add(input.readLine()); // Host: thishost.csc.ncsu.edu
					//message.add(input.readLine()); // Port: 5678
//					if(!message.get(0).equals(VERSION)) {
//						output.println(responseCode(-1));
//						break;
//					}
					
					System.out.println("------------------- List all request from peer -------------------");
					for(int i = 0; i < message.size(); i++)
						System.out.println(message.get(i));
					
					output.println(rfcIndexList.size());
					for (RFCIndex rfc : rfcIndexList) {
						for(ActivePeer ap : activePeerList) {
							if(ap.getHostName().equals(rfc.getRFCHostName())) {
								output.println(rfc.getRFCNumber() + " " + rfc.getTitle() + " " + rfc.getRFCHostName() + " " + ap.getIndex());
							}
						}
					}
				} 
				else if (line.startsWith("GET")) {
					System.out.println("------------------- GET request from peer -------------------");
					message.add(line); //Host: one.ncsu.edu
					message.add(input.readLine());
					message.add(input.readLine());
					String clientRequestHostName = message.get(1).split(" ")[1];
					Integer RFCNumber = Integer.valueOf(message.get(2));
					Integer port = null;
					for (RFCIndex rfc : rfcIndexList) {
							for (ActivePeer ap : activePeerList) { //if (rfc.getRFCHostName().equals(clientRequestHostName)
								if (rfc.getRFCHostName().equals(clientRequestHostName) && ap.getHostName().equals(rfc.getRFCHostName()) && RFCNumber.equals(rfc.getRFCNumber())) {
									port = ap.getIndex();
								}
							}
					}
					output.println(String.valueOf(port));
				}
				else if (line.startsWith("LOOKUP")) {
					int code = -2; // not found
					Integer port = null;
					Set<String> set = new HashSet<>();
					message.add(line); // LOOKUP RFC 3457 P2P-CI/1.0
					message.add(input.readLine()); // Host: thishost.csc.ncsu.edu
					message.add(input.readLine()); // Title: Requirements for IPsec Remote Access Scenarios
					
					System.out.println("------------------- Lookup request from peer -------------------");
					for(int i = 0; i < message.size(); i++)
						System.out.println(message.get(i));
					
					Integer clientRequestRFCNumber = Integer.valueOf(message.get(0).split(" ")[2]);
					String clientRequestHostName = message.get(1).split(" ")[1];
					String clientRequestTitle = message.get(2).split(": ")[1];
					for (RFCIndex rfc : rfcIndexList) {
							for (ActivePeer ap : activePeerList) {
								if (ap.getHostName().equals(rfc.getRFCHostName()) && clientRequestRFCNumber.equals(rfc.getRFCNumber())) {
									port = ap.getIndex();
								}
							}

							set.add("Host: " + rfc.getRFCHostName() + "\nPort: " + String.valueOf(port));
							code = 1;
					}
					
					if (code == 1 && port != null) {
						for(String str : set)
							output.println(str);
					} 
					else if(port == null) {
						output.println("No such entry found");
					}
					else {
						output.println(responseCode(code));
					}
					output.println(EOF);
				} else if (line.startsWith("END")) {
					String host_name = input.readLine();	
					if (activePeerList != null) {
						Iterator<ActivePeer> it = activePeerList.iterator();
						while(it.hasNext()) {
							if(it.next().getHostName().equals(host_name))
								it.remove();
						}
					}

					if (rfcIndexList != null) {
						Iterator<RFCIndex> it = rfcIndexList.iterator();
						while(it.hasNext()) {
							if(it.next().getRFCHostName().equals(host_name))
								it.remove();
						}
					}
					
					System.out.println("------------------- End request from peer. Connection closed with the peer -------------------");
					output.println("Connection Closed with Server");
					output.println(EOF);
					socket.close();
					break;
				}
				else {
					System.out.println("------------------- Invalid Request -------------------");
					output.println("Invalid Request");
				}
				
				output.flush();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// ADD RFC 123 P2P-CI/1.0
	// Host: thishost.csc.ncsu.edu
	// Port: 5678
	// Title: A Proferred Official ICP
	public int addRFC(List<String> message, PrintWriter output) {
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
					} 
					if (!split[1].equals("RFC")) {
						return 0;
					} 
					if (!split[3].equals(VERSION)) {
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
		output.println(responseCode(1) + "\nRFC " + RFCNumber + " " + title + " " + RFCHostName + " " + port);
		return 1;
	}
}

default:
	chmod 777 src/p2pclient/Client.java
	chmod 777 src/p2pclient/P2PClient.java
	chmod 777 src/p2pclient/P2PClientAbstract.java
	
	chmod 777 src/p2pserver/Server.java
	chmod 777 src/p2pserver/P2PServer.java
	chmod 777 src/p2pserver/P2PServerAbstract.java
	
	chmod 777 src/p2pserver/ActivePeer.java
	chmod 777 src/p2pserver/ClientInformation.java
	chmod 777 src/p2pserver/RFCIndex.java
	
	sudo apt-get update
	sudo apt-get install default-jdk -y

	sudo cp -pf client_script.sh /usr/bin/Client
	sudo cp -pf server_script.sh /usr/bin/Server
	chmod 777 /usr/bin/Server
	chmod 777 /usr/bin/Client
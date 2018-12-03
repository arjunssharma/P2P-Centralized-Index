default:
	chmod 777 src/*.java
	sudo apt-get update
	sudo apt-get install default-jdk -y
	sudo cp -pf client_script.sh /usr/bin/Client
	sudo cp -pf server_script.sh /usr/bin/Server
	chmod 777 /usr/bin/Server
	chmod 777 /usr/bin/Client
	
	sudo iptables -A INPUT -p tcp --dport 7734 -j ACCEPT
	sudo iptables -A OUTPUT -p tcp --dport 7734 -j ACCEPT
	sudo iptables -A INPUT -p tcp --dport 3000 -j ACCEPT
	sudo iptables -A OUTPUT -p tcp --dport 3000 -j ACCEPT
	sudo iptables -A INPUT -p tcp --dport 4000 -j ACCEPT
	sudo iptables -A OUTPUT -p tcp --dport 4000 -j ACCEPT
	sudo iptables -A INPUT -p tcp --dport 5000 -j ACCEPT
	sudo iptables -A OUTPUT -p tcp --dport 5000 -j ACCEPT
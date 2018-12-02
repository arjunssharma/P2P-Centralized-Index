default:
	chmod 777 src/*.java
	sudo apt-get update
	sudo apt-get install default-jdk -y
	sudo cp -pf client_script.sh /usr/bin/Client
	sudo cp -pf server_script.sh /usr/bin/Server
	chmod 777 /usr/bin/Server
	chmod 777 /usr/bin/Client
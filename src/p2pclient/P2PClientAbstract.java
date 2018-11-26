package p2pclient;

import java.net.ServerSocket;

public abstract class P2PClientAbstract {

	String clientName;
	ServerSocket clientUploadServer;
	public static final String VERSION = "P2P-CI/1.0";
	public static final String BAD_REQUEST = "400 Bad Request";
	public static final String BAD_VERSION = "505 " + VERSION + " Version Not Supported";
	public static final String NOT_FOUND = "404 Not Found";
	public static final String OK_STATUS = VERSION + " 200 OK";
	public static final String EOF = "EOF";
	
	public static String responseCode(int code) {
		if (code == 0)
			return BAD_REQUEST;
		else if (code == -1)
			return BAD_VERSION;
		else if (code == 1)
			return OK_STATUS;
		else if (code == -2)
			return NOT_FOUND;
		else
			return new String("Error with response codes");
	}

}

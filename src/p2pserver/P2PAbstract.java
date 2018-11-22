package p2pserver;

import java.io.PrintWriter;

public class P2PAbstract {

	public final String VERSION = "P2P-CI/1.0";
	public final String BAD_REQUEST = "400 Bad Request";
	public final String BAD_VERSION = "505 " + VERSION + " Version Not Supported";
	public final String NOT_FOUND = "404 Not Found";
	public final String OK_STATUS = VERSION + " 200 OK";

	public void responseCode(PrintWriter output, int code) {
		if (code == 0)
			output.println(BAD_REQUEST);
		else if (code == -1)
			output.println(BAD_VERSION);
		else if (code == 1)
			output.println(OK_STATUS);
		else if (code == -2)
			output.println(NOT_FOUND);
	}
}
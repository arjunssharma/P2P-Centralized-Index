package pojo;

public class ClientInformation {

	private Integer RFCNumber;
	private Integer port;
	private String RFCHostName;

	public ClientInformation(Integer rFCNumber, Integer port, String rFCHostName) {
		super();
		RFCNumber = rFCNumber;
		this.port = port;
		RFCHostName = rFCHostName;
	}

	public Integer getRFCNumber() {
		return RFCNumber;
	}

	public void setRFCNumber(Integer rFCNumber) {
		RFCNumber = rFCNumber;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getRFCHostName() {
		return RFCHostName;
	}

	public void setRFCHostName(String rFCHostName) {
		RFCHostName = rFCHostName;
	}

}

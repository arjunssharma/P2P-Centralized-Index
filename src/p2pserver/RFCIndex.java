package p2pserver;

public class RFCIndex {

	private Integer RFCNumber;
	private String title;
	private String RFCHostName;

	public RFCIndex(Integer RFCNumber, String title, String RFCHostName) {
		super();
		this.RFCNumber = RFCNumber;
		this.title = title;
		this.RFCHostName = RFCHostName;
	}

	public Integer getRFCNumber() {
		return RFCNumber;
	}

	public void setRFCNumber(Integer rFCNumber) {
		RFCNumber = rFCNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRFCHostName() {
		return RFCHostName;
	}

	public void setRFCHostName(String rFCHostName) {
		RFCHostName = rFCHostName;
	}

}

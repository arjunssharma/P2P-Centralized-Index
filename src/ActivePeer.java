
/**
 *
 * @author Arjun Sharma
 *
 */

public class ActivePeer {

	private String hostName;
	private Integer index;
	
	public ActivePeer(String hostName, Integer index) {
		super();
		this.hostName = hostName;
		this.index = index;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

}

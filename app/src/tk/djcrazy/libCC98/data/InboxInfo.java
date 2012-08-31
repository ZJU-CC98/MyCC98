package tk.djcrazy.libCC98.data;
/**
 * Store the information of inbox OR outbox.
 * 
 * @author zsy
 *
 */

public class InboxInfo {
	private int totalPmIn = 0;
	private int totalInPage = 0;
	
	public InboxInfo(int totalPmIn, int totalInPage){
		this.setTotalPmIn(totalPmIn).setTotalInPage(totalInPage);
	}
	
	public InboxInfo setTotalPmIn(int totalPmIn){
		this.totalPmIn = totalPmIn;
		return this;
	}
	
	public InboxInfo setTotalInPage(int totalInPage){
		this.totalInPage = totalInPage;
		return this;
	}
	
	public int getTotalPmIn() {
		return totalPmIn;
		
	}
	
	public int getTotalInPage() {
		return totalInPage;
	}
	
}

package tk.djcrazy.libCC98.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Store the information of inbox OR outbox.
 * 
 * @author zsy
 *
 */

public class InboxInfo {
	private int totalPmIn = 0;
	private int totalInPage = 0;
    private List<PmInfo> pmInfos = new ArrayList<PmInfo>();

    public List<PmInfo> getPmInfos() {
        return pmInfos;
    }

    public void setPmInfos(List<PmInfo> pmInfos) {
        this.pmInfos = pmInfos;
    }

    public InboxInfo() {

    }
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

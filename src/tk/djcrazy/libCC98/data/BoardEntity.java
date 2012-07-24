/**
 * 
 */
package tk.djcrazy.libCC98.data;

/**
 * @author DJ
 *
 */
public class BoardEntity {
	/**
	 * @return the boardName
	 */
	public String getBoardName() {
		return boardName;
	}
	/**
	 * @param boardName the boardName to set
	 */
	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}
	/**
	 * @return the boardID
	 */
	public int getBoardID() {
		return boardID;
	}
	/**
	 * @param boardID the boardID to set
	 */
	public void setBoardID(int boardID) {
		this.boardID = boardID;
	}
	/**
	 * @return the boardIntro
	 */
	public String getBoardIntro() {
		return BoardIntro;
	}
	/**
	 * @param boardIntro the boardIntro to set
	 */
	public void setBoardIntro(String boardIntro) {
		BoardIntro = boardIntro;
	}
	/**
	 * @return the lastReplyTopicName
	 */
	public String getLastReplyTopicName() {
		return lastReplyTopicName;
	}
	/**
	 * @param lastReplyTopicName the lastReplyTopicName to set
	 */
	public void setLastReplyTopicName(String lastReplyTopicName) {
		this.lastReplyTopicName = lastReplyTopicName;
	}
	/**
	 * @return the lastReplyTopicID
	 */
	public int getLastReplyTopicID() {
		return lastReplyTopicID;
	}
	/**
	 * @param lastReplyTopicID the lastReplyTopicID to set
	 */
	public void setLastReplyTopicID(int lastReplyTopicID) {
		this.lastReplyTopicID = lastReplyTopicID;
	}
	/**
	 * @return the lastReplyAuthor
	 */
	public String getLastReplyAuthor() {
		return lastReplyAuthor;
	}
	/**
	 * @param lastReplyAuthor the lastReplyAuthor to set
	 */
	public void setLastReplyAuthor(String lastReplyAuthor) {
		this.lastReplyAuthor = lastReplyAuthor;
	}
	private String boardName="";
	private int boardID=0;
	private String BoardIntro="";
	private String lastReplyTopicName="";
	private int lastReplyTopicID=0;
	private String lastReplyAuthor=""; 
	private String lastReplyTime="";
	private int postNumberToday=0;
	private String boardMaster="";
	/**
	 * @return the boardMaster
	 */
	public String getBoardMaster() {
		return boardMaster;
	}
	/**
	 * @param boardMaster the boardMaster to set
	 */
	public void setBoardMaster(String boardMaster) {
		this.boardMaster = boardMaster;
	}
	/**
	 * @return the postNumberToday
	 */
	public int getPostNumberToday() {
		return postNumberToday;
	}
	/**
	 * @param postNumberToday the postNumberToday to set
	 */
	public void setPostNumberToday(int postNumberToday) {
		this.postNumberToday = postNumberToday;
	}
	/**
	 * @return the lastReplyTime
	 */
	public String getLastReplyTime() {
		return lastReplyTime;
	}
	/**
	 * @param lastReplyTime the lastReplyTime to set
	 */
	public void setLastReplyTime(String lastReplyTime) {
		this.lastReplyTime = lastReplyTime;
	}
	private String lastReplyTopicLink="";
	/**
	 * @return the lastReplyTopicLink
	 */
	public String getLastReplyTopicLink() {
		return lastReplyTopicLink;
	}
	/**
	 * @param lastReplyTopicLink the lastReplyTopicLink to set
	 */
	public void setLastReplyTopicLink(String lastReplyTopicLink) {
		this.lastReplyTopicLink = lastReplyTopicLink;
	}
	private String boardLink="";
	/**
	 * @return the boardLink
	 */
	public String getBoardLink() {
		return boardLink;
	}
	/**
	 * @param boardLink the boardLink to set
	 */
	public void setBoardLink(String boardLink) {
		this.boardLink = boardLink;
	}
}

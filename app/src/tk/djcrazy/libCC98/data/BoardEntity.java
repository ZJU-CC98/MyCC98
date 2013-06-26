/**
 * 
 */
package tk.djcrazy.libCC98.data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author DJ
 *
 */
public class BoardEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String boardName="";
	private String boardID="0";
	private String BoardIntro="";
	private String lastReplyTopicName="";
	private String lastReplyTopicID="0";
	private String lastReplyAuthor=""; 
	private Date lastReplyTime;
	private String lastReplyBoardId;
	private int postNumberToday=0;
	private String boardMaster="";
	private int childBoardNumber=0;
  
 	public String getBoardName() {
		return boardName;
	}
 	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}
 	public String getBoardID() {
		return boardID;
	}
	/**
	 * @param boardID the boardID to set
	 */
	public void setBoardID(String boardID) {
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
	public String getLastReplyTopicID() {
		return lastReplyTopicID;
	}
	/**
	 * @param lastReplyTopicID the lastReplyTopicID to set
	 */
	public void setLastReplyTopicID(String lastReplyTopicID) {
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
	public Date getLastReplyTime() {
		return lastReplyTime;
	}
	/**
	 * @param lastReplyTime the lastReplyTime to set
	 */
	public void setLastReplyTime(Date lastReplyTime) {
		this.lastReplyTime = lastReplyTime;
	}
	/**
	 * @return the childBoardNumber
	 */
	public int getChildBoardNumber() {
		return childBoardNumber;
	}
	/**
	 * @param childBoardNumber the childBoardNumber to set
	 */
	public void setChildBoardNumber(int childBoardNumber) {
		this.childBoardNumber = childBoardNumber;
	}
	
	public String getChildBoardString() {
		String res = childBoardNumber!=0  ? ("  ("+childBoardNumber+")"):"";
		return res;
	}
	/**
	 * @return the lastReplyBoardId
	 */
	public String getLastReplyBoardId() {
		return lastReplyBoardId;
	}
	/**
	 * @param lastReplyBoardId the lastReplyBoardId to set
	 */
	public void setLastReplyBoardId(String lastReplyBoardId) {
		this.lastReplyBoardId = lastReplyBoardId;
	}
 }

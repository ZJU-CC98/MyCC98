/**
 * 
 */
package tk.djcrazy.libCC98.data;

import java.util.Date;

/**
 * @author DJ
 *
 */
public class PostEntity {
	/**
	 * @return the postType
	 */
	public String getPostType() {
		return postType;
	}
	/**
	 * @param postType the postType to set
	 */
	public void setPostType(String postType) {
		this.postType = postType;
	}
	/**
	 * @return the postName
	 */
	public String getPostName() {
		return postName;
	}
	/**
	 * @param postName the postName to set
	 */
	public void setPostName(String postName) {
		this.postName = postName;
	}
	/**
	 * @return the postLink
	 */
	public String getPostLink() {
		return postLink;
	}
	/**
	 * @param postLink the postLink to set
	 */
	public void setPostLink(String postLink) {
		this.postLink = postLink;
	}
	/**
	 * @return the postPageNumber
	 */
	public int getPostPageNumber() {
		return postPageNumber;
	}
	/**
	 * @param postPageNumber the postPageNumber to set
	 */
	public void setPostPageNumber(int postPageNumber) {
		this.postPageNumber = postPageNumber;
	}
	/**
	 * @return the postAuthorName
	 */
	public String getPostAuthorName() {
		return postAuthorName;
	}
	/**
	 * @param postAuthorName the postAuthorName to set
	 */
	public void setPostAuthorName(String postAuthorName) {
		this.postAuthorName = postAuthorName;
	}
	/**
	 * @return the replyNumber
	 */
	public String getReplyNumber() {
		return replyNumber;
	}
	/**
	 * @param replyNumber the replyNumber to set
	 */
	public void setReplyNumber(String replyNumber) {
		this.replyNumber = replyNumber;
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
	 * @return the lastReplyLink
	 */
	public String getLastReplyLink() {
		return lastReplyLink;
	}
	/**
	 * @param lastReplyLink the lastReplyLink to set
	 */
	public void setLastReplyLink(String lastReplyLink) {
		this.lastReplyLink = lastReplyLink;
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
	private String postType= "";
	private String postName= "";
	private String postLink= "";
	private int postPageNumber=0;
	private String postAuthorName= "";
	private String replyNumber= "";
	private Date lastReplyTime;
	private String lastReplyLink= "";
	private String lastReplyAuthor= "";
}

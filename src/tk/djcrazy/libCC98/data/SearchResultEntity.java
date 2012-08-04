package tk.djcrazy.libCC98.data;

import java.util.Date;

 
public class SearchResultEntity {
	/**
	 * @return the faceId
	 */
	public String getFaceId() {
		return faceId;
	}
	/**
	 * @param faceId the faceId to set
	 */
	public void setFaceId(String faceId) {
		this.faceId = faceId;
	}
	/**
	 * @return the boardId
	 */
	public String getBoardId() {
		return boardId;
	}
	/**
	 * @param boardId the boardId to set
	 */
	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
	/**
	 * @return the postId
	 */
	public String getPostId() {
		return postId;
	}
	/**
	 * @param postId the postId to set
	 */
	public void setPostId(String postId) {
		this.postId = postId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}
	/**
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	/**
	 * @return the postTime
	 */
	public Date getPostTime() {
		return postTime;
	}
	/**
	 * @param postTime the postTime to set
	 */
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
	private String faceId ;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NewPostEntity [faceId=" + faceId + ", boardId=" + boardId
				+ ", postId=" + postId + ", title=" + title + ", authorName="
				+ authorName + ", postTime=" + postTime + "]";
	}
 	public String getTotalResult() {
		return totalResult;
	}
 	public void setTotalResult(String totalResult) {
		this.totalResult = totalResult;
	}
	private String boardId;
	private String postId;
	private String title;
	private String authorName; 
	private Date postTime;
	private String totalResult;
 }

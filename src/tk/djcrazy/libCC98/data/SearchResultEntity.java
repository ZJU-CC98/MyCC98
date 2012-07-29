package tk.djcrazy.libCC98.data;

import java.util.Date;

 
public class SearchResultEntity {
	/**
	 * @return the faceId
	 */
	public int getFaceId() {
		return faceId;
	}
	/**
	 * @param faceId the faceId to set
	 */
	public void setFaceId(int faceId) {
		this.faceId = faceId;
	}
	/**
	 * @return the boardId
	 */
	public int getBoardId() {
		return boardId;
	}
	/**
	 * @param boardId the boardId to set
	 */
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	/**
	 * @return the postId
	 */
	public int getPostId() {
		return postId;
	}
	/**
	 * @param postId the postId to set
	 */
	public void setPostId(int postId) {
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
	private int faceId ;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NewPostEntity [faceId=" + faceId + ", boardId=" + boardId
				+ ", postId=" + postId + ", title=" + title + ", authorName="
				+ authorName + ", postTime=" + postTime + "]";
	}
	private int boardId;
	private int postId;
	private String title;
	private String authorName; 
	private Date postTime;
 }

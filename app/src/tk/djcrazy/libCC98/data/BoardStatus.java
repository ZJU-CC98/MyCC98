package tk.djcrazy.libCC98.data;

/**
 * Store the board info
 * 
 * @author DJ
 * 
 */
public class BoardStatus  {
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
	 * @return the totalPostToday
	 */
	public int getTotalPostToday() {
		return totalPostToday;
	}
	/**
	 * @param totalPostToday the totalPostToday to set
	 */
	public void setTotalPostToday(int totalPostToday) {
		this.totalPostToday = totalPostToday;
	}
	/**
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public float getPostNumberPercentage() {
		if (totalPostToday==0) {
			return 0;
		} else {
			return (float) (postNumberToday*1.0/totalPostToday);
		}
	}
	private String boardId="";
	private String boardName="";
	private int postNumberToday=0;
	private int totalPostToday=1;
 	private int rating=1;	
 }

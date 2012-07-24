package tk.djcrazy.MyCC98.data;

/**
 * Store the board info
 * 
 * @author DJ
 * 
 */
public class BoardInfo {

    private String boardName="";

    private String boardLink="";

    private String intro="";

    private String lastReplyTopicName="";

    private String lastReplyTopicLink="";

    private String lastReplyAuthor="";

    private String lastReplyTime="";

    private String lastReplyLink="";

    private int postNumberToday=0;

    public BoardInfo() {
        
    }
    private BoardInfo(Builder builder) {
        boardName = builder.boardName;
        boardLink = builder.boardLink;
        intro = builder.intro;
        lastReplyTopicName = builder.lastReplyTopicName;
        lastReplyAuthor = builder.lastReplyAuthor;
        lastReplyTime = builder.lastReplyTime;
        postNumberToday = builder.postNumberToday;
        lastReplyTopicLink = builder.lastReplyTopicLink;
        lastReplyLink = builder.lastReplyLink;
    }

    /**
     * Use this Builder to Obtain a new BoardInfo instance
     * 
     * @author DJ
     * 
     */
    public static class Builder {

        private String boardName = "";


        private String boardLink = "";

        private String intro = "";

        private String lastReplyTopicName = "";

        private String lastReplyTopicLink = "";

        private String lastReplyAuthor = "";

        private String lastReplyTime = "";

        private String lastReplyLink = "";

        private int postNumberToday = 0;

        public Builder() {

        }

        public Builder boardName(String arg) {
            boardName = arg;
            return this;
        }

        public Builder boardLink(String arg) {
            boardLink = arg;
            return this;
        }

        public Builder intro(String arg) {
            intro = arg;
            return this;
        }

        public Builder lastReplyTopicLink(String arg) {
            lastReplyTopicLink = arg;
            return this;
        }

        public Builder lastReplyTopicName(String arg) {
            lastReplyTopicName = arg;
            return this;
        }

        public Builder lastReplyAuthor(String arg) {
            lastReplyAuthor = arg;
            return this;
        }

        public Builder lastReplyTime(String arg) {
            lastReplyTime = arg;
            return this;
        }

        public Builder lastReplyLink(String arg) {
            lastReplyLink = arg;
            return this;
        }

        public Builder postNumberToday(int arg) {
            postNumberToday = arg;
            return this;
        }

        public BoardInfo build() {
            return new BoardInfo(this);
        }

    }

    public String getBoardName() {
        return boardName;
    }

    public String getBoardLink() {
        return boardLink;
    }

    public String getIntro() {
        return intro;
    }

    public String getLastReplyTopicName() {
        return lastReplyTopicName;
    }

    public String getLastReplyTopicLink() {
        return lastReplyTopicLink;
    }

    public String getLastReplyAuthor() {
        return lastReplyAuthor;
    }

    public String getLastReplyTime() {
        return lastReplyTime;
    }

    public String getLastReplyLink() {
        return lastReplyLink;
    }

    public int getPostNumberToday() {
        return postNumberToday;
    }
    
    
    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    
    public void setBoardLink(String boardLink) {
        this.boardLink = boardLink;
    }

    
    public void setIntro(String intro) {
        this.intro = intro;
    }

    
    public void setLastReplyTopicName(String lastReplyTopicName) {
        this.lastReplyTopicName = lastReplyTopicName;
    }

    
    public void setLastReplyTopicLink(String lastReplyTopicLink) {
        this.lastReplyTopicLink = lastReplyTopicLink;
    }

    
    public void setLastReplyAuthor(String lastReplyAuthor) {
        this.lastReplyAuthor = lastReplyAuthor;
    }

    
    public void setLastReplyTime(String lastReplyTime) {
        this.lastReplyTime = lastReplyTime;
    }

    
    public void setLastReplyLink(String lastReplyLink) {
        this.lastReplyLink = lastReplyLink;
    }

    
    public void setPostNumberToday(int postNumberToday) {
        this.postNumberToday = postNumberToday;
    }

}

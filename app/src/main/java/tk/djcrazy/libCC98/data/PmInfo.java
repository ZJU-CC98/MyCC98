package tk.djcrazy.libCC98.data;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Store the information of pm.
 * 
 * 
 */

public final class PmInfo {

	// Is this pm a new one?
    private boolean newPm;

    // The sender
    private String from;

    // The topic of the pm.
    private String topic;

    // The id CC98 assigned to the pm.
    private int pmId;

    // Sending time.
    private String time;

    // Sender/receiver's avatar URL.
    // Used in MyCC98 Client for performance.
    private String avatarUrl;
    
    // The content of the pm
    private String content;

    //private static final String TAG = "PmInfo";

    private PmInfo(boolean newPm, String form, String topic, int pmId,
            String time, String content, String avatarUrl) {
        this.newPm = newPm;
        this.from = form;
        this.topic = topic;
        this.pmId = pmId;
        this.time = time;
        this.avatarUrl = avatarUrl;
        this.content = content;
    }

    private PmInfo(Builder builder) {
        newPm = builder.newTopic;
        from = builder.fromWho;
        topic = builder.topicTile;
        pmId = builder.pmId;
        time = builder.sendTime;
        avatarUrl = builder.userAvater;
    }

    
    @Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this);
    }
    /**
     * PLease use this Builder to obtain a new instance of PmInfo. e.g: PmInfo =
     * PmInfo.Builder.newTopic(arg).fromWho(arg).....userAvatar(arg).build();
     * 
     * @author DJ
     * 
     */
    public static class Builder {

        private boolean newTopic = false;
        private String fromWho = "";
        private String topicTile = "";
        private int pmId = -1;
        private String sendTime = "2010/10/10";
        private String userAvater = null;
        private String content = "";

        public Builder(int arg) {
        	pmId = arg;
        }

        public Builder newTopic(boolean arg) {
            newTopic = arg;
            return this;
        }

        public Builder fromWho(String arg) {
            fromWho = arg;
            return this;
        }

        public Builder topicTitle(String arg) {
            topicTile = arg;
            return this;
        }

        public Builder sendTime(String arg) {
            sendTime = arg;
            return this;
        }

        public Builder userAvatar(String arg) {
            userAvater = arg;
            return this;
        }
        
        public Builder content(String arg){
        	content = arg;
        	return this;
        }

        public PmInfo build() {
            return new PmInfo(this);
        }
    }

    public boolean isNew() {
        return newPm;
    }

    public String getSender() {
        return from;
    }

    public String getTopic() {
        return topic;
    }

    public int getPmId() {
        return pmId;
    }

    public String getSendTime() {
        return time;
    }
    
    public String getUserAvatar() {
        return avatarUrl;
    }
    
    public String getContent(){
    	return content;
    }
}

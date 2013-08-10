/**
 * 
 */
package tk.djcrazy.libCC98.data;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author DJ
 *
 */
public class UserProfileEntity {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
	 * @return the userAvatarLink
	 */
	public String getUserAvatarLink() {
		return userAvatarLink;
	}
	/**
	 * @param userAvatarLink the userAvatarLink to set
	 */
	public void setUserAvatarLink(String userAvatarLink) {
		this.userAvatarLink = userAvatarLink;
	}
	/**
	 * @return the userNickName
	 */
	public String getUserNickName() {
		return userNickName;
	}
	/**
	 * @param userNickName the userNickName to set
	 */
	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}
	/**
	 * @return the userLevel
	 */
	public String getUserLevel() {
		return userLevel;
	}
	/**
	 * @param userLevel the userLevel to set
	 */
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	/**
	 * @return the userGroup
	 */
	public String getUserGroup() {
		return userGroup;
	}
	/**
	 * @param userGroup the userGroup to set
	 */
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	/**
	 * @return the goodPosts
	 */
	public String getGoodPosts() {
		return goodPosts;
	}
	/**
	 * @param goodPosts the goodPosts to set
	 */
	public void setGoodPosts(String goodPosts) {
		this.goodPosts = goodPosts;
	}
	/**
	 * @return the totalPosts
	 */
	public String getTotalPosts() {
		return totalPosts;
	}
	/**
	 * @param totalPosts the totalPosts to set
	 */
	public void setTotalPosts(String totalPosts) {
		this.totalPosts = totalPosts;
	}
	/**
	 * @return the userPrestige
	 */
	public String getUserPrestige() {
		return userPrestige;
	}
	/**
	 * @param userPrestige the userPrestige to set
	 */
	public void setUserPrestige(String userPrestige) {
		this.userPrestige = userPrestige;
	}
	/**
	 * @return the registerTime
	 */
	public String getRegisterTime() {
		return registerTime;
	}
	/**
	 * @param registerTime the registerTime to set
	 */
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	/**
	 * @return the loginTimes
	 */
	public String getLoginTimes() {
		return loginTimes;
	}
	/**
	 * @param loginTimes the loginTimes to set
	 */
	public void setLoginTimes(String loginTimes) {
		this.loginTimes = loginTimes;
	}
	/**
	 * @return the deletedPosts
	 */
	public String getDeletedPosts() {
		return deletedPosts;
	}
	/**
	 * @param deletedPosts the deletedPosts to set
	 */
	public void setDeletedPosts(String deletedPosts) {
		this.deletedPosts = deletedPosts;
	}
	/**
	 * @return the deletedRatio
	 */
	public String getDeletedRatio() {
		return deletedRatio;
	}
	/**
	 * @param deletedRatio the deletedRatio to set
	 */
	public void setDeletedRatio(String deletedRatio) {
		this.deletedRatio = deletedRatio;
	}
	/**
	 * @return the lastLoginTime
	 */
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	/**
	 * @param lastLoginTime the lastLoginTime to set
	 */
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	/**
	 * @return the userGender
	 */
	public String getUserGender() {
		return userGender;
	}
	/**
	 * @param userGender the userGender to set
	 */
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	/**
	 * @return the userBirthday
	 */
	public String getUserBirthday() {
		return userBirthday;
	}
	/**
	 * @param userBirthday the userBirthday to set
	 */
	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}
	/**
	 * @return the userConstellation
	 */
	public String getUserConstellation() {
		return userConstellation;
	}
	/**
	 * @param userConstellation the userConstellation to set
	 */
	public void setUserConstellation(String userConstellation) {
		this.userConstellation = userConstellation;
	}
	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}
	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	/**
	 * @return the userQQ
	 */
	public String getUserQQ() {
		return userQQ;
	}
	/**
	 * @param userQQ the userQQ to set
	 */
	public void setUserQQ(String userQQ) {
		this.userQQ = userQQ;
	}
	/**
	 * @return the userMSN
	 */
	public String getUserMSN() {
		return userMSN;
	}
	/**
	 * @param userMSN the userMSN to set
	 */
	public void setUserMSN(String userMSN) {
		this.userMSN = userMSN;
	}
	/**
	 * @return the userPage
	 */
	public String getUserPage() {
		return userPage;
	}
	/**
	 * @param userPage the userPage to set
	 */
	public void setUserPage(String userPage) {
		this.userPage = userPage;
	}
	/**
	 * @return the bbsMasterInfo
	 */
	public String getBbsMasterInfo() {
		return bbsMasterInfo;
	}
	/**
	 * @param bbsMasterInfo the bbsMasterInfo to set
	 */
	public void setBbsMasterInfo(String bbsMasterInfo) {
		this.bbsMasterInfo = bbsMasterInfo;
	}
	/**
	 * @return the onlineTime
	 */
	public String getOnlineTime() {
		return onlineTime;
	}
	/**
	 * @param onlineTime the onlineTime to set
	 */
	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}
	private String userAvatarLink="";
	private String userNickName="";
	private String userLevel="";
	private String userGroup="";
	private String goodPosts="";
	private String totalPosts="";
	private String userPrestige="";
	private String registerTime="";
	private String loginTimes="";
	private String deletedPosts="";
	private String deletedRatio="";
	private String lastLoginTime="";
	private String userGender="";
	private String userBirthday="";
	private String userConstellation="";
	private String userEmail="";
	private String userQQ="";
	private String userMSN="";
	private String userPage="";
	private String bbsMasterInfo="";
	private String onlineTime="";
}

package tk.djcrazy.MyCC98.data;

import java.util.List;

import tk.djcrazy.libCC98.data.PostContentEntity;

public class PostContentsListPage {
	private List<PostContentEntity> contentList = null;
	private String contentString = null;
	
	public List<PostContentEntity> getList() {
		return contentList;
	}
	
	public String getString() {
		return contentString;
	}
	
	public PostContentsListPage setString(String contentString) {
		this.contentString = contentString;
		return this;
	}
	
	public PostContentsListPage setList(List<PostContentEntity> contentList2) {
		this.contentList = contentList2;
		return this;
	}
}

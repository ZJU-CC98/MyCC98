package tk.djcrazy.MyCC98.data;

import java.util.List;

import tk.djcrazy.libCC98.data.PmInfo;

public class PmListPage {
	private List<PmInfo> pmList = null;
	private String pageString = null;

	public PmListPage() {
	}

	public List<PmInfo> getList() {
		return pmList;
	}

	public String getString() {
		return pageString;
	}

	public PmListPage setString(String content) {
		pageString = content;
		return this;
	}

	public PmListPage setList(List<PmInfo> list) {
		pmList = list;
		return this;
	}
}

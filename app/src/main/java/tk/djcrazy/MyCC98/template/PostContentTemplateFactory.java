package tk.djcrazy.MyCC98.template;

public class PostContentTemplateFactory {
 	
	public static PostcontentTemplate getDefault() {
		return DefaultPostContentTemplate.getInstance();
	}
	
	public static PostcontentTemplate getLifetoy() {
		return LifetoyPostContentTemplate.getInstance();
	}
	public static PostcontentTemplate getSimple() {
		return SimplePostContentTemplate.getInstance();
	}
}
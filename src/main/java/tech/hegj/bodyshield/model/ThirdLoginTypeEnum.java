package tech.hegj.bodyshield.model;

public enum ThirdLoginTypeEnum{
	WX(1,"微信"),
	QQ(2,"QQ"),
	FACEBOOK(3,"facebook");
	private int id;
	private String desc;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	private ThirdLoginTypeEnum(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}
}
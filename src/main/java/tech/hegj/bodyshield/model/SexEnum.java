package tech.hegj.bodyshield.model;

public enum SexEnum{
	MAN(0,"男"),
	WOMAN(1,"女"),
	SECRECY(2,"保密");
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
	
	private SexEnum(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}
}
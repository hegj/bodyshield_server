package tech.hegj.bodyshield.model;

public class User {
	private int id;
	private String name;
	private String password;
	private String mobile;
	private String email;
	private int sex;
	private int age;
	private long birthday;
	private long createTime;
	private String wxOpenid;
	private String qqOpenid;
	private String facebookOpenid;
	private String headImg;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getBirthday() {
		return birthday;
	}
	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getWxOpenid() {
		return wxOpenid == null ? "" : wxOpenid;
	}
	public void setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
	}
	public String getQqOpenid() {
		return qqOpenid== null ? "" : qqOpenid;
	}
	public void setQqOpenid(String qqOpenid) {
		this.qqOpenid = qqOpenid;
	}
	public String getFacebookOpenid() {
		return facebookOpenid == null ? "" : facebookOpenid;
	}
	public void setFacebookOpenid(String facebookOpenid) {
		this.facebookOpenid = facebookOpenid;
	}
	public String getHeadImg() {
		return headImg == null ? "" : headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
}

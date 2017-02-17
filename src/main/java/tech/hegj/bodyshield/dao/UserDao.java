package tech.hegj.bodyshield.dao;

import tech.hegj.bodyshield.model.User;

/**
 * user
 * @author hegj
 *
 */
public interface UserDao {
	public int add(User user) throws Exception;
	public User getByName(String name) throws Exception;
	public User get(int uid) throws Exception;
	public User login(String name, String password) throws Exception;
	public User getByOpenid(String openid, int thirdLoginType) throws Exception;
	public int updateHeadImg(int uid, String img) throws Exception;
}

package tech.hegj.bodyshield.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import tech.hegj.bodyshield.dao.BaseDao;
import tech.hegj.bodyshield.dao.UserDao;
import tech.hegj.bodyshield.model.ThirdLoginTypeEnum;
import tech.hegj.bodyshield.model.User;
import tech.hegj.bodyshield.utils.DateUtils;

@Repository
public class UserDaoImpl extends BaseDao implements UserDao{
	private RowMapper<User> rowMapper = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int arg1) throws SQLException {
			User entity = new User();
			entity.setId(rs.getInt("id"));
			entity.setName(rs.getString("name"));
			entity.setPassword(rs.getString("password"));
			entity.setMobile(rs.getString("mobile"));
			entity.setEmail(rs.getString("email"));
			entity.setSex(rs.getInt("sex"));
			entity.setAge(rs.getInt("age"));
			if(rs.getTimestamp("birthday") != null){
				entity.setBirthday(rs.getTimestamp("birthday").getTime());
			}
			if(rs.getTimestamp("create_time") != null){
				entity.setCreateTime(rs.getTimestamp("create_time").getTime());
			}
			entity.setWxOpenid(rs.getString("wx_openid"));
			entity.setQqOpenid(rs.getString("qq_openid"));
			entity.setFacebookOpenid(rs.getString("facebook_openid"));
			entity.setHeadImg(rs.getString("head_img"));
			return entity;
		}
	};

	@Override
	public int add(User user) throws Exception {
		StringBuilder sql = new StringBuilder("insert into user(`name`, `password`, `mobile`, `email`, `sex`, `age`, `birthday`, `create_time`, `wx_openid`, `qq_openid`, `facebook_openid`, `head_img`) ");
		sql.append("values(?,?,?,?,?,?,?,?,?,?,?,?) ");
		Object[] params = { user.getName(), user.getPassword(), user.getMobile(), user.getEmail(), user.getSex(),
				user.getAge(), DateUtils.getDateFormat(user.getBirthday()),
				DateUtils.getDateFormat(user.getCreateTime()), user.getWxOpenid(), user.getQqOpenid(), user.getFacebookOpenid(), user.getHeadImg()};
		return insert(sql.toString(), params);
	}
	
	public User getByName(String name) throws Exception{
		StringBuilder sql = new StringBuilder("select * from user ");
		sql.append("where name=? ");
		Object[] params = {name};
		
		return queryForObject(sql.toString(), rowMapper, params);
	}

	@Override
	public User login(String name, String password) throws Exception {
		StringBuilder sql = new StringBuilder("select * from user ");
		sql.append("where name=? ");
		sql.append("and password=? ");
		Object[] params = {name,password};
		return queryForObject(sql.toString(), rowMapper, params);
	}

	@Override
	public User getByOpenid(String openid, int thirdLoginType) throws Exception {
		StringBuilder sql = new StringBuilder("select * from user ");
		sql.append("where 1=1 ");
		if(thirdLoginType == ThirdLoginTypeEnum.WX.getId()){
			sql.append("and wx_openid=? ");
		}else if (thirdLoginType == ThirdLoginTypeEnum.QQ.getId()) {
			sql.append("and qq_openid=? ");
		}else if (thirdLoginType == ThirdLoginTypeEnum.FACEBOOK.getId()) {
			sql.append("and facebook_openid=? ");
		}else {
			return null;
		}
		Object[] params = {openid};
		return queryForObject(sql.toString(), rowMapper, params);
	}

	@Override
	public int updateHeadImg(int uid, String img) throws Exception {
		StringBuilder sql = new StringBuilder("update user ");
		sql.append("set head_img=? ");
		sql.append("where id=? ");
		Object[] params = {img, uid};
		return jdbcTemplate.update(sql.toString(), params);
	}

	@Override
	public User get(int uid) throws Exception {
		StringBuilder sql = new StringBuilder("select * from user ");
		sql.append("where id=? ");
		Object[] params = {uid};
		
		return queryForObject(sql.toString(), rowMapper, params);
	}

	@Override
	public int updatePassword(String name, String password) throws Exception {
		StringBuilder sql = new StringBuilder("update user ");
		sql.append("set password=? ");
		sql.append("where name=? ");
		Object[] params = {password, name};
		return jdbcTemplate.update(sql.toString(), params);
	}
}
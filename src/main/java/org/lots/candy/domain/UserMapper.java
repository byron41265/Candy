package org.lots.candy.domain;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.lots.candy.entity.TwitterProfile;
import org.lots.candy.entity.User;

@Mapper
public interface UserMapper {
	
	@Update("update user set ${provider}Account = #{userProviderId} where userId =#{userId}")
	public void updateAccount(@Param("userId")String userId, @Param("provider")String provider, @Param("userProviderId")String userProviderId);
	
	@Select("select * from user s where s.email = #{email} and s.password = md5(#{password}) and s.status='1'")
	public User findUserByEmailAndPwd(@Param("email") String email, @Param("password") String password);
	
	@Select("select * from user s where s.email = #{email}")
	public User findUserByEmail(@Param("email") String email);
	
	@Insert("insert into user(userId,username,password,wallet,email,status) values(#{userId},#{username},md5(#{password}),#{email},#{status})")
	public void save(@Param("userId") String userId, @Param("username") String username, @Param("password") 
	String password, @Param("email") String email, @Param("status") String status);
	
	@Update("update user set status = '1' where userId = #{userId}")
	public void updateUserStatus(@Param("userId") String userId);

}

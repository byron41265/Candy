package org.lots.candy.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	@Select("select * from user s where s.email = #{email} and s.password = md5(#{password}) and status='1'")
	public User findUserByEmailAndPwd(@Param("email") String email, @Param("password") String password);
	
	@Select("select * from user where ${column} = #{value} and status='1'")
	public User findUserByElement(@Param("column") String column, @Param("value") String value);
	
	@Select("select count(*) from user where inviteCode=#{superInviteCode} and status='1'")
	public int findInviteCode(@Param("superInviteCode") String superInviteCode);
	
	@Insert("insert into user(userId,username,password,email,inviteCode,superInviteCode,status) values(#{userId},#{username},md5(#{password}),#{email},#{inviteCode},#{superInviteCode},#{status})")
	public void save(@Param("userId") String userId, @Param("username") String username, @Param("password") 
	String password, @Param("email") String email, @Param("inviteCode")String inviteCode, @Param("superInviteCode") String superInviteCode, @Param("status") String status);
	
	@Update("update user set status = '1' where userId = #{userId}")
	public void updateUserStatus(@Param("userId") String userId);
	
	@Update("update user set password = md5(#{password}) where userId=#{userId}")
	public void resetPassword(@Param("userId") String userId, @Param("password") String password);
	
	@Update("update user set wallet = #{wallet} where userId=#{userId}")
	public void addWallet(@Param("wallet") String wallet, @Param("userId") String userId);
	
	@Select("select * from user where superInviteCode=#{inviteCode} and status='1'")
	public List<User> findUserBySuperInviteCode(@Param("inviteCode") String inviteCode);
	
	@Select("select count(*) from user where superInviteCode=#{superInviteCode}")
	public int findCodeTotalNum(@Param("superInviteCode") String superInviteCode);
	
	@Select("select * from influence_point order by level")
	public List<HashMap> findInfluencePoint();
	
	@Select("select ifnull(sum(earned_point),0) from action where userId=#{userId}")
	public int getPointByUserId(@Param("userId") String userId);
	
	@Select("select count(distinct a.user1) as num1, count(distinct a.user2) as num2, "
			+ " count(distinct a.user3) as num3, count(distinct a.user4) as num4,"
			+ " count(distinct a.user5) as num5, count(distinct a.user6) as num6,"
			+ " count(distinct a.user7) as num7, count(distinct a.user8) as num8 from"
			+ " (select t1.userId as user1,"
			+ "t2.userId as user2,"
			+ "t3.userId as user3,"
			+ "t4.userId as user4,"
			+ "t5.userId as user5,"
			+ "t6.userId as user6,"
			+ "t7.userId as user7,"
			+ "t8.userId as user8"
			+ " from user as t"
			+ " left join user as t1"
			+ " on t.inviteCode = t1.superInviteCode and t1.status='1'"
			+ " left join user as t2"
			+ " on t1.inviteCode = t2.superInviteCode and t2.status='1'"
			+ " left join user as t3"
			+ " on t2.inviteCode = t3.superInviteCode and t3.status='1'"
			+ " left join user as t4"
			+ " on t3.inviteCode = t4.superInviteCode and t4.status='1'"
			+ " left join user as t5"
			+ " on t4.inviteCode = t5.superInviteCode and t5.status='1'"
			+ " left join user as t6"
			+ " on t5.inviteCode = t6.superInviteCode and t6.status='1'"
			+ " left join user as t7"
			+ " on t6.inviteCode = t7.superInviteCode and t7.status='1'"
			+ " left join user as t8"
			+ " on t7.inviteCode = t8.superInviteCode and t8.status='1'"
			+ " where t.userId = #{userId}) a")
	public HashMap findInfluencePeople(@Param("userId") String userId);
}

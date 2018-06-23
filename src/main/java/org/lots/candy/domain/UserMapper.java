package org.lots.candy.domain;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
	
	@Update("update user set ${provider}Account = #{userProviderId} where userId =#{userId}")
	public void updateAccount(@Param("userId")String userId, @Param("provider")String provider, @Param("userProviderId")String userProviderId);

}

package org.lots.candy.domain;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TelegramMapper {
	
	@Insert("replace into telegram_invite (fromUserId, invitedUserId) values (#{fromUserId}, #{invitedUserId} )")
	public void insertInvitation(@Param("fromUserId")String fromUserId, @Param("invitedUserId")String invitedUserId);

}

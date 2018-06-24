package org.lots.candy.domain;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.lots.candy.entity.TwitterProfile;

@Mapper
public interface TwitterMapper {
	
	@Update("replace into twitter_profile (`userId`,`providerUserId`,`name`,`screenName`,`friendCount`,`followerCount`) "
			+ "VALUES (#{userId },#{providerUserId },#{name },#{screenName },#{friendCount },#{followerCount });")
	public void updateTwitterProfile(TwitterProfile profile);
	
	@Delete("truncate table  twitter_follower")
	public void cleanOfficialFollowerId();
	
	@Insert("<script>insert into twitter_follower values <foreach item='item' collection='list'  separator=',' > (#{item}) </foreach></script>")
	public void insertOfficialFollowerId(List<Long> ids);
	
	@Insert("replace into twitter_retweet(tweetId, providerUserId) values(#{tweetId},#{providerUserId})")
	public void updateRetweet(@Param("tweetId")String tweetId, @Param("providerUserId")String providerUserId);

}

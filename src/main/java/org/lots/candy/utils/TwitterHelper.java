package org.lots.candy.utils;

import java.util.List;

import org.lots.candy.config.Constant;
import org.lots.candy.domain.DictMapper;
import org.lots.candy.domain.TaskMapper;
import org.lots.candy.domain.TwitterMapper;
import org.lots.candy.domain.UserMapper;
import org.lots.candy.entity.Dict;
import org.lots.candy.entity.TaskEnum;
import org.lots.candy.entity.TwitterProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

@Component
public class TwitterHelper {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private TaskMapper taskMapper;
	
	@Autowired
	private DictMapper dictMapper;
	
	@Autowired
	private TwitterMapper twitterMapper;
	
	public  void saveTwitterUser(String userId , Twitter twitter){
		String userProviderId = String.valueOf(twitter.userOperations().getUserProfile().getId());
		userMapper.updateAccount(userId, "twitter", userProviderId);
		taskMapper.insertOnceTask(userId, TaskEnum.BINDTWITTER.toString());
		
		TwitterProfile profile = new TwitterProfile(); 
		profile.setUserId(userId);
		profile.setProviderUserId(userProviderId);
		profile.setName(twitter.userOperations().getUserProfile().getName());
		profile.setScreenName(twitter.userOperations().getUserProfile().getScreenName());
		profile.setFollowerCount(twitter.userOperations().getUserProfile().getFollowersCount());
		profile.setFriendCount(twitter.userOperations().getUserProfile().getFriendsCount());
		
		twitterMapper.updateTwitterProfile(profile);
		if("admin".equals(userId)){
			
			// 获取当前follower数据
			CursoredList<Long> followerIds =  twitter.friendOperations().getFollowerIds();
			twitterMapper.cleanOfficialFollowerId();
			while(followerIds != null && followerIds.size()!=0){
				twitterMapper.insertOfficialFollowerId(followerIds);
				if (followerIds.hasNext()){
					followerIds = twitter.friendOperations().getFollowerIdsInCursor(followerIds.getNextCursor());
				}else{
					followerIds =  null;
				}
			}
			
			// 获取转发数据
			List<Dict> dicts =  dictMapper.queryByType(Constant.SOCIAL_TWITTER_TWEETS);
			
			for(Dict dic : dicts){
				Long tweetId = Long.valueOf(dic.getValue());
				// 只能获取最近100个转发
				List<Tweet> tweets =  twitter.timelineOperations().getRetweets(tweetId);
				
				for(Tweet t : tweets){
					String providerUserId = String.valueOf(t.getFromUserId());
					twitterMapper.updateRetweet(tweetId.toString(), providerUserId);
				}
				
			}
			
			
			
		}else {
			
			
		}
		
		
		
	}

}

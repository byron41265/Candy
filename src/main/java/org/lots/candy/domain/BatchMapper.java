package org.lots.candy.domain;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BatchMapper {
	
	@Update("replace into user_task (userId, taskId, earnedPoint)"
			+ "select p.userId, t.taskId, t.eachPoint from twitter_follower f join twitter_profile p on f.providerUserId = p.providerUserId join task t on t.taskId ='2'")
	public void updateUserTaskTwitter();
	
	@Update("replace into action (`userId`,`taskId`,`earned_point`,`submitUrl`,`submitTime`,`if_effective`,`if_handled`)"
			+ "select u.userId, t.taskId, t.eachPoint, i.invitedUserId, i.invitedate,'Y','Y'  from telegram_invite i join user u on i.fromUserId = u.telegramAccount join task t on t.taskId = '9'")
	public void updateAction();
	
	@Update("replace into user_task ( userId , taskId, earnedPoint)"
			+ "select f.userId , f.taskId ,( case when sum(f.dailyEarnedPoint) > f.pointLimit then f.pointLimit else sum(f.dailyEarnedPoint) end) from "
			+ "(select a.userId, a.taskId, a.submitTime, (case when a.earned_point > t.dailyPointLimit then t.dailyPointLimit else a.earned_point end) dailyEarnedPoint, t.pointLimit from  ("
			+ "select a.userId, a.taskId, DATE_FORMAT( a.submitTime, '%Y-%m-%d' ) submitTime, sum(a.earned_point)  earned_point "
			+ "from action a where a.if_effective ='Y' group by a.userId, a.taskId, DATE_FORMAT( a.submitTime, '%Y-%m-%d' ) "
			+ ")  a join task t on a.taskId = t.taskId) f group by f.userId, f.taskId , f.pointLimit")
	public void updateUserTask();
	
	@Update("update user us, (select @rownum := @rownum+1 as rownum,"
			+ "A.userId, "
			+ "if(@total=sumPoint,@rank,@rank:=@rownum) as r, "
			+ "@total:=sumPoint from "
			+ "(select ifnull(sum(t.earnedPoint),0) sumPoint, t.userId from user_task t group by t.userId order by sumPoint desc) A "
			+ ",(select @rank:=0,@rownum:=0,@total:=null) B) tm "
			+ "set us.rank = tm.r where us.userId = tm.userId")
	public void updateRank();
}

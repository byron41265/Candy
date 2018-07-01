use candy;



-- 关注（follow）twitter账号
replace into user_task (userId, taskId, earnedPoint)
select p.userId, t.taskId, t.eachPoint from twitter_follower f join twitter_profile p on f.providerUserId = p.providerUserId join task t on t.taskId ='2';

-- 转发任务
replace into action (`userId`,`taskId`,`earned_point`,`submitUrl`,`submitTime`,`if_effective`,`if_handled`)
select p.userId, t.taskId, t.eachPoint, r.tweetId submitUrl, r.retweetdate ,'Y' ,'Y'   from twitter_retweet r join twitter_profile p on r.providerUserId = p.providerUserId join task t on t.taskId = '3';

-- telegram 拉群
replace into action (`userId`,`taskId`,`earned_point`,`submitUrl`,`submitTime`,`if_effective`,`if_handled`)
select u.userId, t.taskId, t.eachPoint, i.invitedUserId, i.invitedate,'Y','Y'  from telegram_invite i join user u on i.fromUserId = u.telegramAccount join task t on t.taskId = '9';


-- 提交链接的多次任务 
replace into user_task ( userId , taskId, earnedPoint)
select f.userId , f.taskId ,( case when sum(f.dailyEarnedPoint) > f.pointLimit then f.pointLimit else sum(f.dailyEarnedPoint) end) from 
(select a.userId, a.taskId, a.submitTime, (case when a.earned_point > t.dailyPointLimit then t.dailyPointLimit else a.earned_point end) dailyEarnedPoint, t.pointLimit from  (

select a.userId, a.taskId, DATE_FORMAT( a.submitTime, "%Y-%m-%d" ) submitTime, sum(a.earned_point)  earned_point
from action a where a.if_effective ='Y' group by a.userId, a.taskId, DATE_FORMAT( a.submitTime, "%Y-%m-%d" ) 


)  a join task t on a.taskId = t.taskId) f group by f.userId, f.taskId , f.pointLimit;




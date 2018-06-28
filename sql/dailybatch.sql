use candy;

select * from user_task;

-- 关注（follow）twitter账号
replace into user_task (userId, taskId, earnedPoint)
select p.userId, t.taskId, t.eachPoint from twitter_follower f join twitter_profile p on f.providerUserId = p.providerUserId join task t on t.taskId ='2';

-- 设置




select * from user for update
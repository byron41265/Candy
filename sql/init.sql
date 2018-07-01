drop database candy;

CREATE DATABASE IF NOT EXISTS candy DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

drop user 'candy'@'%';

CREATE USER 'candy'@'%' IDENTIFIED BY 'candy';

drop user 'candy'@'localhost';

CREATE USER 'candy'@'localhost' IDENTIFIED BY 'candy';

GRANT ALL PRIVILEGES ON candy.* TO 'candy'@'%';

GRANT ALL PRIVILEGES ON candy.* TO 'candy'@'localhost';

FLUSH PRIVILEGES;

use candy;

drop table userconnection;

create table userconnection (
	userId varchar(100) not null,
	providerId varchar(50) not null,
	providerUserId varchar(100) not null,
	rank int not null,
	displayName varchar(255),
	profileUrl varchar(512),
	imageUrl varchar(512),
	accessToken varchar(512) not null,
	secret varchar(512),
	refreshToken varchar(512),
	expireTime bigint
) ENGINE=MYISAM;

ALTER TABLE userconnection add primary key (userId, providerId, providerUserId);

drop table user;

create table user (
	userId varchar(100) not null,
    userName varchar(100) not null,
	`password` varchar(100) not null,
	wallet varchar(1000),
	email varchar(100),
	twitterAccount varchar(100),
	facebookAccount varchar(100),
    telegramAccount varchar(100),
	inviteCode varchar(512),
	superInviteCode varchar(512),
	status varchar(2)
) ENGINE=MYISAM;

ALTER TABLE user add primary key (userId);

drop table task;

create table task (
	taskId varchar(100) not null,
    `name` varchar(100) not null, 
	eachPoint int not null,
	pointLimit int not null,
	dailyPointLimit  int not null,
	instruction varchar(2000),
	ifClosed varchar(2),
	ifEffective varchar(2),
	checkMethod varchar(32) COMMENT '1 自动校验  2 手工校验 ',
	startDate date,
	endDate date,
    rank int
) ENGINE=MYISAM;
ALTER TABLE task add primary key (taskId);

INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`)
 VALUES ('1', 'Please bind Twitter', '5', '5', '5', 'You didn\'t bind Twitter yet, please click the menubar icon of Twitter to bind.', 'N', 'Y', '1', 0);
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`)
 VALUES ('2', 'Please follow Official Twitter', '5', '5', '5', '', 'N', 'Y', '1', 1);
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`)
 VALUES ('3', 'Please retweet Official Tweet', '5', '50', '5', '', 'N', 'Y', '1', 2);
  
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`)
 VALUES ('4', 'Please like Official Tweet', '1', '20', '20', '', 'N', 'N', '2', 3); 
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`)
 VALUES ('5', 'Write Blogs/Articles/Reviews for us', '200', '2000', '200', '', 'N', 'Y', '2', 4);  

INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`)
 VALUES ('6', 'Make Youtube video for us', '200', '2000', '200', '', 'N', 'Y', '2', 5);  
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`)
 VALUES ('7', 'Please bind Facebook', '5', '5', '5', 'You didn\'t bind Facebook yet, please click the menubar icon of Facebook to bind.', 'N', 'Y', '1', 6);   
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`)
 VALUES ('8', 'Please bind Telegram', '5', '5', '5', 'You didn\'t bind Telegram yet, please click the menubar icon of Telegram to bind.', 'N', 'Y', '1', 7);    
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`)
 VALUES ('9', 'Please invite you friends to Telegram group', '5', '500', '50', '', 'N', 'Y', '1', 8);     
 
 commit;
 
drop table user_task;

create table user_task (
	userId varchar(100) not null,
	taskId varchar(100) not null,
    earnedPoint int DEFAULT 0
) ENGINE=MYISAM;
 
ALTER TABLE user_task add primary key (userId, taskId); 
 

drop table action;
create table action (
	userId varchar(100) not null,
	taskId varchar(100) not null,
	earned_point int DEFAULT 0,
	submitUrl varchar(512),
	submitTime datetime default NOW(),
	if_effective varchar(2),
	if_handled varchar(2)
) ENGINE=MYISAM;

ALTER TABLE action add primary key (userId, taskId, submitTime); 

drop table dic_item;

create table dic_item (
	dicId varchar(100) not null,
    dicTypeId varchar(100) default 'common',
    `value` varchar(512),
    `desc` varchar(512)
) ENGINE=MYISAM;

ALTER TABLE dic_item add primary key (dicId);

-- twitter Id
INSERT INTO `dic_item` (`dicId`, `dicTypeId`, `value`) VALUES ('social.twitter.tweets.01', 'social.twitter.tweets', '990911032292884480');
INSERT INTO `dic_item` (`dicId`, `dicTypeId`, `value`) VALUES ('social.twitter.tweets.02', 'social.twitter.tweets', '1012824857992679424');

-- Telegram group 
INSERT INTO `dic_item` (`dicId`, `dicTypeId`, `value`, `desc`) VALUES ('social.telegram.group.01', 'social.telegram.group', 'https://t.me/joinchat/IOi2LREmcW8xzJgwO5hceg', 'test group');


drop table twitter_profile;

create table twitter_profile(
	userId varchar(100) not null,
	providerUserId varchar(100) not null,
    `name` varchar(100),
    screenName varchar(100),
    friendCount int,
    followerCount int
) ENGINE=MYISAM;

ALTER TABLE twitter_profile add primary key (userId, providerUserId);

drop table twitter_follower;

create table twitter_follower(
	providerUserId varchar(100) not null
) ENGINE=MYISAM;

ALTER TABLE twitter_follower add primary key (providerUserId);

drop table twitter_retweet;

create table twitter_retweet(
	tweetId varchar(100) not null,
    providerUserId varchar(100) not null,
    retweetdate datetime default NOW()
) ENGINE=MYISAM;

ALTER TABLE twitter_retweet add primary key (tweetId, providerUserId);

drop table telegram_invite;

create table telegram_invite(
	fromUserId  varchar(100) not null,
    invitedUserId   varchar(100) not null,
    invitedate datetime default NOW()
) ENGINE=MYISAM;

ALTER TABLE telegram_invite add primary key (fromUserId, invitedUserId);








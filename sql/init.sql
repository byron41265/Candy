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
	status varchar(2),
	`rank` int
) ENGINE=MYISAM;

ALTER TABLE user add primary key (userId);

drop table task_type;

create table task_type (
`typeId` varchar(20),
fullname varchar(50),
shortname varchar(20)
	
) ENGINE=MYISAM;

ALTER TABLE task_type add primary key (typeId);

INSERT INTO `task_type`(`typeId`,`fullname`,`shortname`) VALUES('A','Binding Bounty','Binding');
INSERT INTO `task_type`(`typeId`,`fullname`,`shortname`) VALUES('B','Retweet Bounty','Retweet');
INSERT INTO `task_type`(`typeId`,`fullname`,`shortname`) VALUES('C','Content Creation Bounty','Content');
INSERT INTO `task_type`(`typeId`,`fullname`,`shortname`) VALUES('D','Artistic Design Bounty','Artistic');
INSERT INTO `task_type`(`typeId`,`fullname`,`shortname`) VALUES('E','Telegram Master Bounty','Telegram');
INSERT INTO `task_type`(`typeId`,`fullname`,`shortname`) VALUES('F','Invite Your Friends','Inviting');

commit;

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
    rank int,
    `phase` int,
    `typeId` varchar(20)
) ENGINE=MYISAM;
ALTER TABLE task add primary key (taskId);

INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('1', 'Bind Twitter', '5', '5', '5', 'You didn\'t bind Twitter yet, please click the menubar icon of Twitter to bind.', 'N', 'Y', '1', 2, '1' , 'A');
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('2', 'Follow our Twitter', '5', '5', '5', '', 'N', 'Y', '1', 4 , '1' , 'B');
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('3', 'RETWEET', '5', '50', '5', '', 'N', 'Y', '1', 5 , '1' , 'B');
  
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('4', 'Please like Official Tweet', '1', '20', '20', '', 'N', 'N', '2', 3 , '1' , ''); 
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('5', 'Blogs articles', '200', '2000', '200', '', 'N', 'Y', '2', 6 , '1' , 'C');  

INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('6', 'Youtube video', '200', '2000', '200', '', 'N', 'Y', '2', 7 , '1' , 'C');  
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('7', 'Bind Facebook', '5', '5', '5', 'You didn\'t bind Facebook yet, please click the menubar icon of Facebook to bind.', 'N', 'Y', '1', 3 , '1' , 'A');   
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('8', 'Bind Telegram', '5', '5', '5', 'You didn\'t bind Telegram yet, please click the menubar icon of Telegram to bind.', 'N', 'Y', '1', 1 , '1' , 'A');    
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('9', 'Telegram inviting', '5', '500', '50', '', 'N', 'Y', '1', 11 , '1' , 'E');     
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('10', 'Quiz in telegram', '50', '500', '500', 'Please follow our official telegram group, we will held quiz events periodically.', 'N', 'Y', '1', 12 , '1' , 'E');      
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('11', 'Please find bug for our product', '100', '1000', '500', 'Please submit bug report to support@lots.org.', 'N', 'Y', '1', 10 , '2' , '');     
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('12', 'Meme & Emoji Design', '200', '2000', '200', '', 'N', 'Y', '2', 8 , '1' , 'D');   
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('13', 'LOGO Filter Design', '200', '2000', '200', '', 'N', 'Y', '2', 9 , '1' , 'D');   
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('14', 'Chinese name design', '200', '2000', '200', '', 'N', 'Y', '2', 10 , '1' , 'D');   
 
INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`, `rank`, `phase`, `typeId`)
 VALUES ('15', 'Invite your friend to BTY', '5', '100', '100', 'Please click the menubar icon of Link. Copy the url and send to your friend.', 'N', 'Y', '1', 13 , '1' , 'F');    
 
 
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

drop table influence_point;

create table influence_point(
	`level`	int,
	`levelName` varchar(50),
	`point`	int,
	`rate`	float
) ENGINE=MYISAM;


insert into influence_point values('1','First dimension','5','0.85');
insert into influence_point values('2','Second dimension','3','0.64');
insert into influence_point values('3','Third dimension','1','0.51');
insert into influence_point values('4','Fourth dimension','1','0.45');
insert into influence_point values('5','Fifth dimension','1','0.32');
insert into influence_point values('6','Sixth dimension','1','0.28');
insert into influence_point values('7','Seventh dimension','1','0.20');
insert into influence_point values('8','Eighth dimension','1','0.15');
commit;

drop table user_ip;

create table user_ip(
	userId varchar(100),
    ip varchar(100),
    `port` int,
    `host`  varchar(100),
    op varchar(100),
    opdate datetime default NOW()
);

drop table rankTokens;
create table rankTokens(
	scoreLevel	int,	
	tokens  int
) ENGINE=MYISAM;
insert into rankTokens values(0,0);
insert into rankTokens values(1,100000);
insert into rankTokens values(2,40000);
insert into rankTokens values(3,20000);
insert into rankTokens values(4,10000);
insert into rankTokens values(5,4000);
insert into rankTokens values(6,2000);
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

drop table user_connection;

create table user_connection (
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

ALTER TABLE user_connection add primary key (userId, providerId, providerUserId);

drop table user;

create table user (
	userId varchar(100) not null,
	password varchar(100) not null,
	wallet varchar(1000),
	email varchar(100),
	twitterAccount varchar(100),
	facebookAccount varchar(100),
	inviteCode varchar(512),
	superInviteCode varchar(512)
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
	checkMethod varchar(32),
	startDate date,
	endDate date
) ENGINE=MYISAM;
ALTER TABLE task add primary key (taskId);

INSERT INTO `task` (`taskId`, `name`, `eachPoint`, `pointLimit`, `dailyPointLimit`, `instruction`, `ifClosed`, `ifEffective`, `checkMethod`)
 VALUES ('1', 'Please bind Twitter', '5', '5', '5', 'You didn\'t bind Twitter yet, please click the menubar icon of Twitter to bind.', 'N', 'Y', '1');
 
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
	earned_point int not null,
	submitUrl varchar(512),
	submitTime date,
	if_effective varchar(2),
	if_handled varchar(2)
) ENGINE=MYISAM;
ALTER TABLE action add primary key (userId, taskId);




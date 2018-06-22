drop database candy;

CREATE DATABASE IF NOT EXISTS candy DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

drop user 'candy'@'%';

CREATE USER 'candy'@'%' IDENTIFIED BY 'candy';

GRANT ALL PRIVILEGES ON candy.* TO 'candy'@'%';

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
	eachPoint int not null,
	pointLimit int not null,
	dailyPointLimit  int not null,
	describe varchar(2000),
	if_closed varchar(2),
	if_effective varchar(2),
	check_method varchar(32),
	start_date date，
	end_date date
) ENGINE=MYISAM;
ALTER TABLE task add primary key (taskId);

drop table action;
create table action (
	userId varchar(100) not null,
	taskId varchar(100) not null,
	earned_point int not null,
	submitUrl varchar(512),
	submitTime date,
	if_effective varchar(2),
	if_handled varchar(2)
) ENGINE-MYISAM;
ALTER TABLE task add primary key (userId, taskId);


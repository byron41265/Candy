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


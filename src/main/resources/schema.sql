CREATE TABLE missions (
	id LONG PRIMARY KEY AUTO_INCREMENT,
	title VARCHAR(50),
	gadget1 VARCHAR(50),
	gadget2 VARCHAR(50),
	dateActive DATETIME,
	duration int,
	isActive int
);

CREATE TABLE agents (
	agentID LONG PRIMARY KEY AUTO_INCREMENT,
	agentName VARCHAR(50),
	agentActive int
);

CREATE TABLE agentMissions (
	id LONG PRIMARY KEY AUTO_INCREMENT,
	agentID LONG,
	missionID LONG
);
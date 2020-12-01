INSERT INTO missions (title, gadget1, gadget2, dateActive, duration, isActive)
VALUES('Standby in Rome', 'Passport', 'Gold Ring', null, 1, 0),
	('Assist the Elderly', 'Blackjack', 'Handcuffs', null, 1, 0),
	('Thought police patrol', 'Razor wit', 'Dashing style: A guidebook', null, 1, 0),
	('Marching orders', 'Switchblade', 'Damp gloves', null, 1, 0);

INSERT INTO agents(agentName, agentActive)
VALUES('Dirst the Worst', 0),
	('Manchester the Slamchester', 0),
	('Jim', 0);
	
INSERT INTO agentMissions(agentID, missionID)
VALUES(1, 1),
	(1, 2),
	(2, 3),
	(1, 4);
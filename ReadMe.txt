Manage Agents and their Missions

	//  -=|  How the app works   |=-  \\

Each Mission can only belong to one Agent at a time
Each Agent can hold multiple Missions

New Agents and Missions can be created
Existing Agents and Missions can be edited or deleted

When a Mission is "Launched", it will become active for its duration
While a Mission is active, it and its Agent will be unavailable to edit or delete.
Other Missions under that Agent can't be launched until the active Mission completes.
Other Missions can still be edited or deleted. This means you can move them to another agent and launch there.

The red "Quit" button performs a system.exit(0) command. [CURRENTLY DISABLED]
This is useful if the program is turned into a .jar or .exe file.

	//  -=|  Notes   |=-  \\

The time displayed is based on SERVER TIME. The servers appear to be in the GMT timezone.

The 'countdown' for active missions is static, because it reads directly from the database.
With JavaScript this can be upgraded to a real-time countdown. 

The file "system.properties" file is used to assist Heroku in using the proper Java version

Images are (clearly) hand drawn

This application originated as a simple project in Sheridan College,
but has been expanded upon immensely.
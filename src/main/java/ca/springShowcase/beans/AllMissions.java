package ca.springShowcase.beans;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An object used to hold all variables when merging Agent and Mission
 * @author cor_b
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllMissions {

	private String agentName;
	private Long agentID;	 
	private Long missionID;  //missionID, same as id
	private int agentActive; //if agent is on a mission
	private Long id;		 //must match missionID, otherwise suggests database issues
	private String agent;	 //must match agentName, otherwise suggests database issues
	private String title;	
	private String gadget1;	
	private String gadget2; 
	private LocalDateTime dateActive;
	private int duration;
	private String timeRemaining; //calculated to the view, not included in database
	private int isActive;
}

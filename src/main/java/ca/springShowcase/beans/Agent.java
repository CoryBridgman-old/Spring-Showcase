package ca.springShowcase.beans;

import lombok.Data;

/**
 * The Agent object
 * @author Cory Bridgman
 * November 2020
 */
@Data
public class Agent {

	private String agentName;
	private Long agentID;
	private Long missionID;
	private int agentActive;
	
	/**
	 * When no name is given, assign a John/Jane Doe to keep the identity safe
	 */
	public void autoFill() {
		if(agentName == null || agentName.trim() == "") {
			double roll = (Math.random()*10)+1;
			if(roll >= 6) {
				agentName = "'John Doe'";
			}else {
				agentName = "'Jane Doe'";
			}
		}
	}
}

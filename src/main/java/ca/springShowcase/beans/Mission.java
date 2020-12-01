package ca.springShowcase.beans;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The main mission object
 * @author Cory Bridgman
 * November 2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mission {
	
	private Long id;		//identify the table row
	private String agent;	//the agent who is assigned the mission. Only used for passing form data
	private String title;	//the title of the mission
	private String gadget1;	//a gadget the agent can bring
	private String gadget2; //a gadget the agent can bring
	private LocalDateTime dateActive;
	private int duration;
	private String timeRemaining; //calculated to the view, not included in database
	private int isActive; //if mission is being acted on by agent
	

	/**
	 * Autofill information which is above your pay grade
	 */
	public void autoFill() {
		if(title == null || title.trim() == "") title = "[TOP SECRET]";
		if(gadget1 == null || gadget1.trim() == "") gadget1 = "[TOP SECRET]";
		if(gadget2 == null || gadget2.trim() == "") gadget2 = "[TOP SECRET]";
	}

}

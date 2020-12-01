package ca.springShowcase.modelAssist;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import ca.springShowcase.beans.Agent;
import ca.springShowcase.beans.AllMissions;
import ca.springShowcase.beans.Mission;
import lombok.Data;

/**
 * A helper class to offload some bulky code
 * @author Cory Bridgman
 *
 */

@Data
@Component
public class Helper {

	private Agent agentMemory;
	
	/**
	 * Assistant method to hold the agent info 
	 * when a page is refreshed, and no agent data is re-sent
	 * 
	 * @param agent
	 * @return Agent object with ID
	 */
	public Agent rememberAgent(Agent agent) {
		if (agent.getAgentID() == null) {
			agent = getAgentMemory();
		}else {
			setAgentMemory(agent);
		}
		return agent;
	}
	
	/**
	 * Check active missions from ALL missions
	 * Return list with updated status
	 * @param allMissions
	 * @return
	 */
	public List<AllMissions> checkActiveAll(List<AllMissions> allMissions) {
		for (AllMissions m : allMissions) {
			if(m.getIsActive() == 2) {
				LocalDateTime testDate = m.getDateActive().plusMinutes(m.getDuration());
				Duration timeRemaining = Duration.between(LocalDateTime.now(), testDate);
				int minutes = (int)timeRemaining.getSeconds()/60;
				int seconds = (int)timeRemaining.getSeconds()%60;
				if(LocalDateTime.now().isAfter(testDate)) {
					m.setTimeRemaining("0");
				}else if(minutes > 0){
					m.setTimeRemaining(minutes + " minutes " + seconds + " seconds");
				}else {
					m.setTimeRemaining(seconds + " seconds");					
				}
			}
		}
		return allMissions;
	}
	
	/**
	 * Check active missions from ALL missions
	 * Return list with updated status
	 * @param missions
	 * @return
	 */
	public List<Mission> checkActive(List<Mission> missions) {
		for (Mission m : missions) {
			if(m.getIsActive() == 2) {
				LocalDateTime testDate = m.getDateActive().plusMinutes(m.getDuration());
				Duration timeRemaining = Duration.between(LocalDateTime.now(), testDate);
				int minutes = (int)timeRemaining.getSeconds()/60;
				int seconds = (int)timeRemaining.getSeconds()%60;
				if(LocalDateTime.now().isAfter(testDate)) {
					m.setTimeRemaining("0");
				}else if(minutes > 0){
					m.setTimeRemaining(minutes + " minutes " + seconds + " seconds");
				}else {
					m.setTimeRemaining(seconds + " seconds");					
				}
			}
		}
		return missions;
	}
	
	/**
	 * Check if agent is active and update mission availability respectively
	 * @param agent
	 * @return
	 */
	public int agentActiveStatus(Agent agent) {
		if(agent.getAgentActive() == 0) {
			return 0;
		}else {
			return 1;			
		}
	}
}

package ca.springShowcase.database;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.springShowcase.beans.Agent;
import ca.springShowcase.beans.AllMissions;
import ca.springShowcase.beans.Mission;
import ca.springShowcase.modelAssist.Helper;

/**
 * The database manipulation class for the App
 * @author Cory Bridgman
 * November 2020
 */
@Repository
public class DatabaseAccess {

	public NamedParameterJdbcTemplate jdbc;
	
	private Helper helper;
	
	/**
	 * Create the jdbc object
	 * @param jdbc
	 */
	public DatabaseAccess(NamedParameterJdbcTemplate jdbc, Helper helper) {
		this.jdbc = jdbc;
		this.helper = helper;
	}

	
	/**
	 * Return all information from database
	 * @return
	 */
	public List<Mission> getMissions(){
		String query = "SELECT * FROM missions";
		
		BeanPropertyRowMapper<Mission> missionMapper = new BeanPropertyRowMapper<Mission>(Mission.class);
		
		List<Mission> missions = jdbc.query(query, missionMapper);
		
		return missions;
	}

	/**
	 * Return all missions as a List
	 * @return
	 */
	public List<AllMissions> getAllMissions(){
		String query = "SELECT mi.*, ag.* FROM missions mi "
				+ "INNER JOIN agentMissions am ON mi.id = am.missionID "
				+ "INNER JOIN agents ag ON am.agentID = ag.agentID "
				+ "ORDER BY ag.agentName";
		
		BeanPropertyRowMapper<AllMissions> missionMapper = new BeanPropertyRowMapper<AllMissions>(AllMissions.class);
		List<AllMissions> allMissions = jdbc.query(query,  missionMapper);
		
		return allMissions;
	}
	
	/**
	 * Return list of missions assigned to a specific agent
	 * @param agentID
	 * @return
	 */
	public List<Mission> getMissionsFor(Long agentID){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM missions "
				+ "WHERE id IN (SELECT missionID FROM agentMissions "
				+ "WHERE agentID = :agentID)";
		namedParameters.addValue("agentID", agentID);
		
		BeanPropertyRowMapper<Mission> missionMapper = new BeanPropertyRowMapper<Mission>(Mission.class);
		List<Mission> missionList = jdbc.query(query, namedParameters, missionMapper);
		return missionList;
	}
	
	/**
	 * Pick and return a mission by its id
	 * Used to match agents to missions
	 * @param missionID
	 * @return
	 */
	public Mission getMissionById(Long missionID){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM missions WHERE id = :missionID";
		namedParameters.addValue("missionID", missionID);
		
		BeanPropertyRowMapper<Mission> missionMapper = new BeanPropertyRowMapper<Mission>(Mission.class);
		Mission missionList = jdbc.query(query, namedParameters, missionMapper).get(0);
		return missionList;
	}
	
	/**
	 * Pick and return an agent by the mission id
	 * Used to match missions to agents
	 * @param missionID
	 * @return
	 */
	public Agent getAgentIdFromMissionId(Long missionID) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM agents "
				+ "WHERE agentID = (SELECT agentID FROM agentMissions "
				+ "WHERE missionID = :missionID)";
		namedParameters.addValue("missionID", missionID);
		
		BeanPropertyRowMapper<Agent> agentMapper = new BeanPropertyRowMapper<Agent>(Agent.class);
		Agent agent = jdbc.query(query, namedParameters,agentMapper).get(0);
		return agent;
	}
	
	/**
	 * Return list of all agents
	 * @return
	 */
	public List<Agent> getAgents() {
		String query = "SELECT * FROM agents";
		
		BeanPropertyRowMapper<Agent> missionMapper = new BeanPropertyRowMapper<Agent>(Agent.class);
		
		List<Agent> agents = jdbc.query(query, missionMapper);
		
		return agents;
	}
	
	/**
	 * Return a specific agent by its id
	 * @param agentID
	 * @return
	 */
	public Agent getAgent(Long agentID) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM agents WHERE agentID = :agentID";
		namedParameters.addValue("agentID", agentID);
		
		BeanPropertyRowMapper<Agent> agentMapper = new BeanPropertyRowMapper<Agent>(Agent.class);
		Agent agent = jdbc.query(query, namedParameters,agentMapper).get(0);
		return agent;
	}
	
	/**
	 * Return an agent based on the selected agent name 
	 * @param mission
	 * @return
	 */
	public Agent getAgentIdFromName(Mission mission) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM agents WHERE agentName = :agent";
		namedParameters.addValue("agent", mission.getAgent());
		
		BeanPropertyRowMapper<Agent> agentMapper = new BeanPropertyRowMapper<Agent>(Agent.class);
		Agent agent = jdbc.query(query, namedParameters,agentMapper).get(0);
		return agent;
	}
	
	
	/**
	 * Insert new mission object into the database
	 * @param mission
	 */
	public void addMissionToTable(Mission mission) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		String query = "INSERT INTO missions (title, gadget1, gadget2, dateActive, duration, isActive) "
				+ "VALUES (:title, :gadget1, :gadget2, CURRENT_TIMESTAMP, :duration, :isActive)";
		mission.autoFill();
		mission.setIsActive(helper.agentActiveStatus(getAgentIdFromName(mission)));
		
		namedParameters.addValue("title", mission.getTitle())
					   .addValue("gadget1", mission.getGadget1())
					   .addValue("gadget2", mission.getGadget2())
					   .addValue("duration", mission.getDuration())
					   .addValue("isActive", mission.getIsActive());

		jdbc.update(query, namedParameters);
	}
	
	/**
	 * Add a mission to an agents list of missions
	 * @param missionID
	 * @param agentID
	 */
	public void addMissionToAgent(Long missionID, Long agentID) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		String query = "INSERT INTO agentMissions (agentID, missionID) VALUES (:agentID, :missionID)";
		namedParameters.addValue("agentID", agentID)
					   .addValue("missionID", missionID);
		
		jdbc.update(query, namedParameters);
	}
	
	/**
	 * Update the selected mission data
	 * @param mission
	 */
	public void updateMission(Mission mission) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		String query = "UPDATE missions SET title = :title, gadget1 = :gadget1, gadget2 = :gadget2, "
					 + "isActive = :isActive, duration = :duration WHERE id = :id";
		mission.autoFill();
		mission.setIsActive(helper.agentActiveStatus(getAgentIdFromName(mission)));
		

		
		namedParameters.addValue("title", mission.getTitle())
					   .addValue("gadget1", mission.getGadget1())
					   .addValue("gadget2", mission.getGadget2())
					   .addValue("duration", mission.getDuration())
					   .addValue("isActive", mission.getIsActive())
					   .addValue("id", mission.getId());

		jdbc.update(query, namedParameters);
	}
	
	/**
	 * Update which agent holds a mission
	 * @param missionID
	 * @param agentID
	 */
	public void updateMissionAgent(Long missionID, Long agentID) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		String query = "UPDATE agentMissions SET agentID = :agentID "
				+ "WHERE missionID = :missionID";
		namedParameters.addValue("agentID", agentID)
			.addValue("missionID", missionID);
		
		jdbc.update(query, namedParameters);
	}
	
	/**
	 * Delete the selected mission by its id
	 * @param id
	 */
	public void deleteMission(Long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String delMissions = "DELETE FROM missions WHERE id = :id";
		String delAgentMns = "DELETE FROM agentMissions WHERE missionID = :id";
		
		namedParameters.addValue("id", id);
		jdbc.update(delMissions, namedParameters);
		jdbc.update(delAgentMns, namedParameters);
	}
	
	/**
	 * Create a new agent
	 * @param agent
	 */
	public void createAgent(Agent agent) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		agent.autoFill();
		String query = "INSERT INTO agents (agentName, agentActive) VALUES (:agentName, :agentActive)";
		namedParameters.addValue("agentName", agent.getAgentName())
					   .addValue("agentActive", 0);
		
		jdbc.update(query, namedParameters);
	}
	
	/**
	 * Update the name of the agent
	 * @param agent
	 */
	public void updateAgent(Agent agent) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		agent.autoFill();
		String query = "UPDATE agents SET agentName = :agentName "
				+ "WHERE agentID = :agentID";
		namedParameters.addValue("agentName", agent.getAgentName())
					.addValue("agentID", agent.getAgentID());

		jdbc.update(query, namedParameters);
	}
	
	/**
	 * Delete an agent, and all references and missions associated with it
	 * @param agentID
	 */
	public void deleteAgent(Long agentID) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", agentID);
		String delMissions 	= "DELETE FROM missions WHERE id IN"
				+ "(SELECT missionID FROM agentMissions WHERE agentID = :id)";
		String delAgent 	= "DELETE FROM agents WHERE agentID = :id";
		String delAgentMns 	= "DELETE FROM agentMissions WHERE agentID = :id";
		
		jdbc.update(delMissions, namedParameters);
		jdbc.update(delAgent, namedParameters);
		jdbc.update(delAgentMns, namedParameters);
	}
	
	/**
	 * Set selected mission as Active
	 * @param missionID
	 * @param agentID
	 */
	public void launchMission(Long missionID, Long agentID) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String setActive = "UPDATE missions SET isActive = 2, dateActive = CURRENT_TIMESTAMP "
				+ "WHERE id = :missionID; "
				+ "UPDATE agents SET agentActive = 1 "
				+ "WHERE agentID = :agentID";
		String setUnavailable = "UPDATE missions SET isActive = 1 "
				+ "WHERE id IN (SELECT missionID FROM agentMissions "
				+ "WHERE agentID = :agentID)";
		
		namedParameters.addValue("missionID", missionID)
					   .addValue("agentID", agentID);

		//set other missions unavailable (do first)
		jdbc.update(setUnavailable, namedParameters);
		//activate mission and agent (do second)
		jdbc.update(setActive, namedParameters);
	}
	
	/**
	 * Complete a selected Active mission
	 * Return unavailable missions to available
	 * @param missionID
	 * @param agentID
	 */
	public void completeMission(Long missionID, Long agentID) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String setActive = "DELETE FROM missions "
				+ "WHERE id = :missionID; "
				+ "UPDATE agents SET agentActive = 0 "
				+ "WHERE agentID = :agentID";
		String setUnavailable = "UPDATE missions SET isActive = 0 "
				+ "WHERE id IN (SELECT missionID FROM agentMissions "
				+ "WHERE agentID = :agentID)";
		
		namedParameters.addValue("missionID", missionID)
					   .addValue("agentID", agentID);

		//set other missions unavailable (do first)
		jdbc.update(setUnavailable, namedParameters);
		//activate mission and agent (do second)
		jdbc.update(setActive, namedParameters);
	}
	
}

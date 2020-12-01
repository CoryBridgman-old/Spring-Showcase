package ca.springShowcase.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ca.springShowcase.database.DatabaseAccess;
import ca.springShowcase.modelAssist.Helper;
import ca.springShowcase.beans.Agent;
import ca.springShowcase.beans.AllMissions;
import ca.springShowcase.beans.Mission;

/**
 * The controller class for the App
 * Handles the site navigation and distributes data as required
 * @author Cory Bridgman
 * November 2020
 */
@Controller
public class HomeController {

	private DatabaseAccess database;
	private Helper helper;
	
	/**
	 * The establish links to the controller object
	 * @param database
	 */
	public HomeController(DatabaseAccess database, Helper helper) {
		this.database = database;
		this.helper = helper;
	}
	
	/**
	 * The home page
	 * @param model
	 * @return
	 */
	@GetMapping("/")
	public String goHome(Model model) {
		List<Agent> agents = database.getAgents();
		
		model.addAttribute("agents", agents)
			.addAttribute("viewMissions", new Agent());
		return "index";
	}
	
	/**
	 * View all missions curently held by selected agent
	 * @param model
	 * @param agent
	 * @return
	 */
	@GetMapping("/viewMissions")
	public String viewMissions(Model model, @ModelAttribute("viewMissions") Agent agent) {
		
		agent = helper.rememberAgent(agent);
		
		List<Mission> missions = database.getMissionsFor(agent.getAgentID());
		missions = helper.checkActive(missions);
		Agent agentName = database.getAgent(agent.getAgentID());
		
		model.addAttribute("agentChosen", agentName)
			.addAttribute("missions", missions);
		
		return "view_missions";
	}
	
	/**
	 * View all missions and all agents on one page
	 * @param model
	 * @return
	 */
	@GetMapping("/viewAllMissions")
	public String viewAllMissions(Model model) {
		
		List<AllMissions> allMissions = database.getAllMissions();
		
		allMissions = helper.checkActiveAll(allMissions);
		
		List<Mission> missionList = database.getMissions();
		model.addAttribute("allMissions", allMissions)
			 .addAttribute("missionList", missionList)
			 .addAttribute("viewMissions", new Agent());
		
		return "view_all_missions";
	}
	
	/**
	 * View missions for selected agent
	 * @param agentID
	 * @param redirectedAgentID
	 * @return
	 */
	@GetMapping("/missionsFor/{agentID}")
	public String missionsFor(@PathVariable Long agentID, RedirectAttributes redirectedAgentID) {
		
		Agent agent = new Agent();
		agent.setAgentID(agentID);
		redirectedAgentID.addFlashAttribute("viewMissions", agent);
		return "redirect:/viewMissions";
	}
	
	/**
	 * Create a new mission object with form data
	 * @param model
	 * @return
	 */
	@GetMapping("/createMission")
	public String createMission(Model model) {
		
		List<Agent> agents = database.getAgents();
		model.addAttribute("agentNames", agents)
			.addAttribute("newMission", new Mission());

		return "create_mission";
	}

	/**
	 * Add new mission to Missions table
	 * Add mission id to agentMissions table
	 * @param mission
	 * @param model
	 * @param redirectedAgentID
	 * @return
	 */
	@PostMapping("/addMission")
	public String addMission( @ModelAttribute ("newMission") Mission mission, Model model,
			RedirectAttributes redirectedAgentID) {
		
		database.addMissionToTable(mission);
		
		Agent agentID = database.getAgentIdFromName(mission);
		Long missionID = database.getMissions().get(database.getMissions().size()-1).getId();
		
		database.addMissionToAgent(missionID, agentID.getAgentID());
		
		redirectedAgentID.addFlashAttribute("viewMissions", agentID);
		return "redirect:/viewMissions";
	}
	
	/**
	 * Edit an existing mission
	 * @param model
	 * @param id
	 * @return
	 */
	@GetMapping("/editMission/{id}")
	public String editMission( Model model, @PathVariable Long id) {
		
		Agent agent = database.getAgent(database.getAgentIdFromMissionId(id).getAgentID());
		List<Agent> agentList = database.getAgents();
		Mission mission = database.getMissionById(id);
		mission.setAgent(database.getAgent(agent.getAgentID()).getAgentName()); ////////////set to correct name
		model.addAttribute("editMission", mission)
			.addAttribute("currentAgent", agent)
			.addAttribute("agentList", agentList);
		
		return "edit_mission";
	}	
	
	/**
	 * Apply the updated mission changes to the database
	 * Redirect to viewMissions
	 * @param mission
	 * @param redirectedAgentID
	 * @return
	 */
	@PostMapping("/updateMission")
	public String updateMision(@ModelAttribute Mission mission, RedirectAttributes redirectedAgentID) {
		database.updateMission(mission);
		Agent agentID = database.getAgentIdFromName(mission);
		database.updateMissionAgent(mission.getId(), agentID.getAgentID());

		redirectedAgentID.addFlashAttribute("viewMissions", agentID);
		return "redirect:/viewMissions";
	}
	
	/**
	 * Delete the selected mission
	 * Redirect to viewMissions
	 * @param id
	 * @param redirectedAgentID
	 * @return
	 */
	@GetMapping("/deleteMission/{id}")
	public String deleteMission(@PathVariable Long id, RedirectAttributes redirectedAgentID) {
		Agent agentID = database.getAgent(database.getAgentIdFromMissionId(id).getAgentID());
		database.deleteMission(id);
		
		redirectedAgentID.addFlashAttribute("viewMissions", agentID);
		
		return "redirect:/viewMissions";
	}
	
	/**
	 * Delete the selected mission
	 * Redirect to viewAllMissions
	 * @param id
	 * @param redirectedAgentID
	 * @return
	 */
	@GetMapping("/deleteMissionFromAll/{id}")
	public String deleteMissionFromAll(@PathVariable Long id/*, RedirectAttributes redirectedAgentID*/) {
		database.deleteMission(id);
		
		return "redirect:/viewAllMissions";
	}
	
	/**
	 * View list of agents
	 * @param model
	 * @return
	 */
	@GetMapping("/viewAgents")
	public String viewAgents(Model model) {
		
		model.addAttribute("agentList", database.getAgents());
		return "view_agents";
	}
	
	/**
	 * Create a new agent form 
	 * @param model
	 * @return
	 */
	@GetMapping("/createAgent")
	public String createAgent(Model model) {
		model.addAttribute("newAgent", new Agent());
		return "create_agent";
	}
	
	/**
	 * Take form from createAgent and apply data to the database
	 * @param agent
	 * @return
	 */
	@PostMapping("/addAgent")
	public String addAgent(@ModelAttribute("newAgent") Agent agent) {
		database.createAgent(agent);
		return "redirect:/viewAgents";
	}
	
	/**
	 * Edit an existing agent form
	 * @param agentID
	 * @param model
	 * @return
	 */
	@GetMapping("/editAgent/{agentID}")
	public String editAgent(@PathVariable Long agentID, Model model) {
		Agent agent = database.getAgent(agentID);
		model.addAttribute("editAgent", agent);
		return "edit_agent";
	}
	
	/**
	 * Update the database information for an existing agent
	 * @param agent
	 * @return
	 */
	@PostMapping("/updateAgent")
	public String updateAgent(@ModelAttribute("editAgent") Agent agent) {
		database.updateAgent(agent);
		return"redirect:/viewAgents";
	}
	
	/**
	 * Delete an agent
	 * @param agentID
	 * @return
	 */
	@GetMapping("/deleteAgent/{agentID}")
	public String deleteAgent(@PathVariable Long agentID) {
		database.deleteAgent(agentID);
		
		return "redirect:/viewAgents";
	}
	
	/**
	 * Launch a mission, return to view All.
	 * Set agent, and other missions under that agent, to unavailable.
	 * @param id
	 * @return
	 */
	@GetMapping("/launchMission/{id}")
	public String launchMission(@PathVariable Long id, RedirectAttributes redirectedAgentID) {
		Agent agentID = database.getAgentIdFromMissionId(id);
		database.launchMission(id, agentID.getAgentID());
		
		redirectedAgentID.addFlashAttribute("viewMissions", agentID);
		return "redirect:/viewMissions";
	}
	
	/**
	 * Launch a mission, return to view All.
	 * Set agent, and other missions under that agent, to unavailable.
	 * @param id
	 * @return
	 */
	@GetMapping("/launchMissionAll/{id}")
	public String launchMissionFromAll(@PathVariable Long id) {
		Long agentID = database.getAgentIdFromMissionId(id).getAgentID();
		database.launchMission(id, agentID);
		return "redirect:/viewAllMissions";
	}
	
	/**
	 * Remove a completed mission.
	 * Set agent, and other missions under that agent, to available.
	 * @param id
	 * @return
	 */
	@GetMapping("/completeMission/{id}")
	public String completeMission(@PathVariable Long id, RedirectAttributes redirectedAgentID) {
		Agent agentID = database.getAgentIdFromMissionId(id);
		database.completeMission(id, agentID.getAgentID());
		
		redirectedAgentID.addFlashAttribute("viewMissions", agentID);
		return "redirect:/viewMissions";
	}
	
	/**
	 * Remove a completed mission, return to view All.
	 * Set agent, and other missions under that agent, to available.
	 * @param id
	 * @return
	 */
	@GetMapping("/completeMissionAll/{id}")
	public String completeMissionAll(@PathVariable Long id) {
		Long agentID = database.getAgentIdFromMissionId(id).getAgentID();
		database.completeMission(id, agentID);
		return "redirect:/viewAllMissions";
	}
	
	/**
	 * Terminate the application
	 */
	@GetMapping("/terminateApplication")
	public void terminateApplication() {
		System.exit(0);
	}
}

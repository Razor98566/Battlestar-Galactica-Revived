package data.missions.BotINReverse;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets
		api.initFleet(FleetSide.ENEMY, "Col", FleetGoal.ESCAPE, false, 10);
		api.initFleet(FleetSide.PLAYER, "CY", FleetGoal.ATTACK, true, 10);

		// Set a blurb for each fleet
		api.setFleetTagline(FleetSide.ENEMY, "Rag tag fleet");
		api.setFleetTagline(FleetSide.PLAYER, "Cylon attack fleet");
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Defeat the Colonial forces!");
		// Set up the player's fleet
		api.addToFleet(FleetSide.ENEMY, "bsg_galactica_variant", FleetMemberType.SHIP, "BSG75 Galactica", true);
		api.addToFleet(FleetSide.ENEMY, "bsg_defender_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_defender_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_defender_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_colonial_one_variant", FleetMemberType.SHIP, "Colonial One", false);
		api.addToFleet(FleetSide.ENEMY, "bsg_cpl_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_cpl_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_cpl_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_cpl_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_erebus_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_erebus_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_erebus_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_erebus_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_tanker_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_tanker_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_freighter_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_freighter_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_vm7_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_vm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_vm7_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_vm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_vm7_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_vm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_vm7_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_vm2_wing", FleetMemberType.FIGHTER_WING, false);
		// Mark player flagship as essential
		api.defeatOnShipLoss("Cavil");
		api.defeatOnShipLoss("Caprica Six");
		api.defeatOnShipLoss("Simon");
		api.defeatOnShipLoss("Sharon");
		
		// Set up the enemy fleet
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP,"Cavil", true);
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP,"Caprica Six", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP,"Simon", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP,"Sharon", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_rm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_rm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_rm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_rm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_rm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_rm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_rm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_rm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_rm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_rm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_rm2_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_hraider_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_hraider_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_hraider_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_hraider_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_hraider_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_hraider_wing", FleetMemberType.FIGHTER_WING, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_hraider_wing", FleetMemberType.FIGHTER_WING, false);
		
		// Set up the map.
		float width = 16000f;
		float height = 25000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
/* 		for (int i = 0; i < 15; i++) {
			float x = (float) Math.random() * width - width/2;
			float y = (float) Math.random() * height - height/2;
			float radius = 100f + (float) Math.random() * 900f; 
			api.addNebula(x, y, radius);
		} 
*/
		
		api.addNebula(8000, 8000, 48000);
		api.addNebula(0, 0, 48000);
		api.addNebula(8000, 0, 48000);
		api.addNebula(0, 8000, 48000);
		// Add an asteroid field
		api.addAsteroidField(0, 0, 90, 16000f,
								20f, 200f, 2500);
		
		// Add some planets.  These are defined in data/config/planets.json.
		// api.addPlanet(0, 0, 200f, "irradiated", 350f, true);
	}

}







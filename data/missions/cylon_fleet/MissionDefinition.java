package data.missions.cylon_fleet;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "CY", FleetGoal.ATTACK, false, 10);
		api.initFleet(FleetSide.ENEMY, "BS", FleetGoal.ATTACK, true, 10);

		// Set a blurb for each fleet
		api.setFleetTagline(FleetSide.PLAYER, "Cylon attack fleet");
		api.setFleetTagline(FleetSide.ENEMY, "Colonial task force");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Defeat all enemy forces");
		api.addBriefingItem(("Cavil's Basestar must survive!"));
		// Set up the player's fleet
		api.addToFleet(FleetSide.ENEMY, "bsg_galactica_variant", FleetMemberType.SHIP, "BSG75 Galactica", true);
		api.addToFleet(FleetSide.ENEMY, "bsg_valkyrie_variant", FleetMemberType.SHIP, "BS Valkyrie", false);
		api.addToFleet(FleetSide.ENEMY, "bsg_mercury_variant", FleetMemberType.SHIP, "BS Pegasus", false);
		api.addToFleet(FleetSide.ENEMY, "bsg_cygnus_variant", FleetMemberType.SHIP, "Orion 1", false);
		api.addToFleet(FleetSide.ENEMY, "bsg_cygnus_variant", FleetMemberType.SHIP, "Orion 2", false);
		api.addToFleet(FleetSide.ENEMY, "bsg_cygnus_variant", FleetMemberType.SHIP, "Orion 3", false);
		api.addToFleet(FleetSide.ENEMY, "bsg_defender_variant", FleetMemberType.SHIP, "Hermes 2", false);
		api.addToFleet(FleetSide.ENEMY, "bsg_defender_variant", FleetMemberType.SHIP, "Hermes 3", false);
		api.addToFleet(FleetSide.ENEMY, "bsg_defender_variant", FleetMemberType.SHIP, "Hermes 1", false);
		api.addToFleet(FleetSide.ENEMY, "bsg_erebus_variant", FleetMemberType.SHIP, "Hermes 2", false);
		api.addToFleet(FleetSide.ENEMY, "bsg_erebus_variant", FleetMemberType.SHIP, "Hermes 3", false);
		api.addToFleet(FleetSide.ENEMY, "bsg_tanker_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_freighter_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_cpl_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_colonial_one_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_defender_variant", FleetMemberType.SHIP, false);

		
		// Set up the enemy fleet
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP, "#-666", true);
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_hbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_hbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_hbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_gbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_gbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_gbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_tosbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_tosbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_tosbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_intruder_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_intruder_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_intruder_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_revenant_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_revenant_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_revenant_variant", FleetMemberType.SHIP, false);

		// Mark player flagship as essential
		api.defeatOnShipLoss("#-666");
		// Set up the map.
		float width = 16000f;
		float height = 16000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		for (int i = 0; i < 15; i++) {
			float x = (float) Math.random() * width - width/2;
			float y = (float) Math.random() * height - height/2;
			float radius = 100f + (float) Math.random() * 900f; 
			api.addNebula(x, y, radius);
		}
		
		api.addNebula(minX + width * 0.8f - 1000, minY + height * 0.4f, 2000);
		api.addNebula(minX + width * 0.8f - 1000, minY + height * 0.5f, 2000);
		api.addNebula(minX + width * 0.8f - 1000, minY + height * 0.6f, 2000);
		
		// Add an asteroid field
		api.addAsteroidField(minX + width * 0.3f, minY, 90, 3000f,
								20f, 70f, 50);
		
		// Add some planets.  These are defined in data/config/planets.json.
		api.addPlanet(0, 0, 200f, "irradiated", 350f, true);
	}
}







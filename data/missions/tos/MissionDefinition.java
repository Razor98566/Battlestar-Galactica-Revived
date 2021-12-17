package data.missions.tos;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "BSG", FleetGoal.ATTACK, false, 20);
		api.initFleet(FleetSide.ENEMY, "Cyl", FleetGoal.ATTACK, true, 20);

		// Set a blurb for each fleet
		api.setFleetTagline(FleetSide.PLAYER, "Colonial Diplomatic Delegation");
		api.setFleetTagline(FleetSide.ENEMY, "Cylon Ambush forces");
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		
		api.addBriefingItem(("Make sure to sign the peace treaty!"));
		api.addBriefingItem(("Either kill all Cylons or make a retreat with the Galactica!"));
		// Set up the player's fleet
		api.addToFleet(FleetSide.PLAYER, "bsg_artemis_variant", FleetMemberType.SHIP, "Galactica", true);
		api.addToFleet(FleetSide.PLAYER, "bsg_artemis_variant", FleetMemberType.SHIP, "Atlantia", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_artemis_variant", FleetMemberType.SHIP, "Acropolis", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_artemis_variant", FleetMemberType.SHIP, "Triton", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_artemis_variant", FleetMemberType.SHIP, "Pacifica", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_artemis_variant", FleetMemberType.SHIP, "Columbia", false);
		// Mark player flagship as essential
		api.defeatOnShipLoss("Galactica");
		
		api.addToFleet(FleetSide.ENEMY, "bsg_tosbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_tosbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_tosbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_tosbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_tosbasestar_variant", FleetMemberType.SHIP, false);

		// api.addToFleet(FleetSide.ENEMY, "bsg_basestar2_variant", FleetMemberType.SHIP, true);
		// api.addToFleet(FleetSide.ENEMY, "bsg_mercury_variant", FleetMemberType.SHIP, true);
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
		api.addPlanet(0, 0, 200f, "barren", 350f, true);
	}

}







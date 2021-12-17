package data.missions.guardian_def;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "CYL", FleetGoal.ATTACK, false, 10);
		api.initFleet(FleetSide.ENEMY, "BSG", FleetGoal.ATTACK, true, 10);

		// Set a blurb for each fleet
		api.setFleetTagline(FleetSide.PLAYER, "Guardians");
		api.setFleetTagline(FleetSide.ENEMY, "Colonial Task Force");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Defeat all enemy forces");
		
		// Set up the player's fleet
		api.addToFleet(FleetSide.PLAYER, "bsg_gbasestar_variant", FleetMemberType.SHIP, true);
		api.addToFleet(FleetSide.PLAYER, "bsg_gbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_gbasestar_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "bsg_gbasestar_variant", FleetMemberType.SHIP, false);
		
		
		// Mark player flagship as essential
		//api.defeatOnShipLoss("BS Galactica");
		
		// Set up the enemy fleet
		api.addToFleet(FleetSide.ENEMY, "bsg_cygnus_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_cygnus_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_cygnus_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_cygnus_variant", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "bsg_cygnus_variant", FleetMemberType.SHIP, false);
		
		
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
		api.addPlanet(0, 0, 200f, "desert", 350f, true);
	}

}







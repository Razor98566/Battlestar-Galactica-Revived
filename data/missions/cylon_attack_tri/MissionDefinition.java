package data.missions.cylon_attack_tri;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "CYL", FleetGoal.ATTACK, true, 10);
		api.initFleet(FleetSide.ENEMY, "Tri", FleetGoal.ATTACK, true, 10);

		// Set a blurb for each fleet
		api.setFleetTagline(FleetSide.PLAYER, "Cylon invasion force");
		api.setFleetTagline(FleetSide.ENEMY, "Eochu Bres defense fleet");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Defeat all enemy forces, crush them all!");
		api.addBriefingItem("Cavil's Basestar must survive!");
		
		// Set up the player's fleet
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP, "Cavil", true);
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP, "Boomer", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP, "Caprica Six", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP, "Leoben", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP, "D'Anna", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP, "Simon", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_basestar2_variant", FleetMemberType.SHIP, "Doral", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_hbasestar_variant", FleetMemberType.SHIP, "Lord Erebus", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_hbasestar_variant", FleetMemberType.SHIP, "Baltar", false);
		api.addToFleet(FleetSide.PLAYER, "bsg_hbasestar_variant", FleetMemberType.SHIP, "Centurion", false);
		// Mark player flagship as essential
		api.defeatOnShipLoss("Cavil");
		
		// Set up the enemy fleet
		api.addToFleet(FleetSide.ENEMY, "paragon_Elite", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "paragon_Elite", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "paragon_Elite", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "paragon_Elite", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "astral_Elite", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "astral_Elite", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "harbinger_Strike", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "harbinger_Strike", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "harbinger_Strike", FleetMemberType.SHIP, false);
		
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
		api.addPlanet(0, 0, 200f, "tundra", 350f, true);
	}

}







package data.missions.random;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;
import java.util.Random;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {
		
		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "BS", FleetGoal.ATTACK, false, 10);
		api.initFleet(FleetSide.ENEMY, "CY", FleetGoal.ATTACK, true, 10);

		// Set a blurb for each fleet
		api.setFleetTagline(FleetSide.PLAYER, "Frendos");
		api.setFleetTagline(FleetSide.ENEMY, "Meanie Beanis");
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Defeat all meanie beanies");
		
		// Set up the player's fleet
		Random rand = new Random();

		// Obtain a number between [0 - 49].
		int n = rand.nextInt(10) + 1;
		api.addToFleet(FleetSide.PLAYER, selectShip(), FleetMemberType.SHIP, true);
		for(int i = 0; i <= n; i++) api.addToFleet(FleetSide.PLAYER, selectShip(), FleetMemberType.SHIP, false); 
		// Mark player flagship as essential
		// api.defeatOnShipLoss("BSG75 Galactica");
		
		// Set up the enemy fleet
		

		// Obtain a number between [0 - 49].
		n = n + rand.nextInt(5);
		for(int i = 0; i <= n; i++) api.addToFleet(FleetSide.ENEMY, selectShip(), FleetMemberType.SHIP, false);
		
		
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
	
	public String selectShip(){


		Random rand = new Random();

		// Obtain a number between [0 - 49].
		int n = rand.nextInt(30);
		String[] ships = new String[]{"bsg_adamant_variant", 
									"bsg_atlas_variant",
									"bsg_berserk_variant",
									"bsg_heracles_variant",
									"bsg_janus_variant",
									"bsg_manticore_variant",
									"bsg_minotaur_variant",
									"bsg_orion_variant",
									"bsg_ranger_variant",
									"bsg_argos_variant",
									"bsg_cerberus_variant",
									"bsg_cratus_variant",
									"bsg_gorgon_variant",
									"bsg_nemesis_variant",
									"bsg_phobos_variant",
									"bsg_gbasestar_variant",
									"bsg_arachne_variant",
									"bsg_cygnus_variant",
									"bsg_mercury_variant",
									"bsg_tosbasestar_variant",
									"bsg_hbasestar_variant",
									"bsg_basestar2_variant",
									"bsg_resurrection_ship_variant",
									"bsg_galactica_variant",
									"bsg_artemis_variant",
									"bsg_raptor_variant",
									"bsg_valkyrie_variant",
									"bsg_erebus_variant",
									"bsg_defender_variant",
									"bsg_intruder_variant",
									"bsg_rm1_tos_variant",
									"bsg_rm1_variant",
									"bsg_colonial_one_variant",
									"bsg_cpl_variant",
									"bsg_freighter_variant",
									"bsg_tanker_variant",
									"bsg_revenant_variant",
									"bsg_hydra_variant",
									"bsg_cera_variant",
									"bsg_talon_variant",};
		return ships[n];
	}

}







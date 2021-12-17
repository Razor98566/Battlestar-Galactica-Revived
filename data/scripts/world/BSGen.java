package data.scripts.world;

import java.awt.Color;
import java.util.List;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.StarTypes;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.SectorProcGen;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin.AsteroidFieldParams;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.campaign.Faction;
import com.fs.starfarer.api.EveryFrameScript;
import data.scripts.world.systems.Cylonia;
import data.scripts.world.systems.NewCaprica;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import exerelin.campaign.SectorManager;

@SuppressWarnings("unchecked")
public class BSGen implements SectorGeneratorPlugin {

	public void generate(SectorAPI sector) {
		boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
		if (!haveNexerelin || SectorManager.getCorvusMode())
			new Cylonia().generate(Global.getSector());
			new NewCaprica().generate(Global.getSector());

		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("bsg_colonies");
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("bsg_cylons");

		FactionAPI bsg_colonies = sector.getFaction("bsg_colonies");
		FactionAPI bsg_cylons = sector.getFaction("bsg_cylons");
		bsg_colonies.setRelationship("hegemony", 100);
		bsg_colonies.setRelationship("tritachyon", -50);
		bsg_colonies.setRelationship("pirates", -100);
		bsg_colonies.setRelationship("independent", 20);
		bsg_colonies.setRelationship("bsg_cylons", -100);
		bsg_colonies.setRelationship("player", 20);
		bsg_cylons.setRelationship("hegemony", -100);
		bsg_cylons.setRelationship("tritachyon", -100);
		bsg_cylons.setRelationship("pirates", -100);
		bsg_cylons.setRelationship("independent", -100);
		bsg_cylons.setRelationship("bsg_colonies", -100);
		bsg_cylons.setRelationship("player", -100);
	}
}

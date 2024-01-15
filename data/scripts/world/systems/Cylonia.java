package data.scripts.world.systems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.impl.campaign.ids.StarTypes;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class Cylonia {
    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Zanus");
        system.getLocation().set(8000, 8000);
        system.setBackgroundTextureFilename("graphics/backgrounds/background1.jpg");

        LocationAPI hyper = Global.getSector().getHyperspace();

        PlanetAPI star = system.initStar("Zanus", // unique id for this star
                StarTypes.RED_GIANT, // id in planets.json
                500f,		// radius (in pixels at default zoom)
                150, // corona radius, from star edge
                5f, // solar wind burn level
                0.5f, // flare probability
                2f); // cr loss mult
        system.setLightColor(new Color(255, 210, 200)); // light color in entire system, affects all entities

        PlanetAPI a1 = system.addPlanet("cylonia", star, "Cylonia", "terran", 50, 150, 4500, 100);
        a1.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "sindria"));
        a1.getSpec().setGlowColor(new Color(255,255,255,255));
        a1.getSpec().setUseReverseLightForGlow(true);
        a1.applySpecChanges();
        a1.setInteractionImage("illustrations", "urban01");
        a1.setFaction("bsg_cylons");

        JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("cy_jp_1", "Cylonia Jump-Point");
        OrbitAPI orbit = Global.getFactory().createCircularOrbit(star, 0, 500, 150);
        jumpPoint1.setOrbit(orbit);
        jumpPoint1.setRelatedPlanet(a1);
        jumpPoint1.setStandardWormholeToHyperspaceVisual();
        jumpPoint1.setCircularOrbit( system.getEntityById("cylonia"), 60, 3000, 100);
        system.addEntity(jumpPoint1);

        PlanetAPI a2 = system.addPlanet("marathon", star, "Marathon", "desert1", 90, 200, 1500, 225);
        a2.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "sindria"));
        a2.getSpec().setGlowColor(new Color(255,255,255,255));
        a2.getSpec().setUseReverseLightForGlow(true);
        a2.applySpecChanges();
        a2.setInteractionImage("illustrations", "desert_moons_ruins");
        a2.setFaction("bsg_cylons");

			SectorEntityToken relay = system.addCustomEntity("marathon_relay", // unique id
					 "Marathon Relay", // name - if null, defaultName from custom_entities.json will be used
					 "comm_relay", // type of object, defined in custom_entities.json
					 "bsg_cylons"); // faction
			relay.setCircularOrbitPointingDown(system.getEntityById("marathon"), 245-60, 4500, 200);

        PlanetAPI a3 = system.addPlanet("lachesis", star, "Lachesis", "frozen", 180, 125, 6000, 150);
        a3.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "sindria"));
        a3.getSpec().setGlowColor(new Color(255,255,255,255));
        a3.getSpec().setUseReverseLightForGlow(true);
        a3.applySpecChanges();
        a3.setInteractionImage("illustrations", "urban01");
        a3.setFaction("bsg_cylons");

        PlanetAPI a4 = system.addPlanet("erebus", star, "Erebus", "jungle", 250, 170, 4000, 365);
        a4.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "sindria"));
        a4.getSpec().setGlowColor(new Color(255,255,255,255));
        a4.getSpec().setUseReverseLightForGlow(true);
        a4.applySpecChanges();
        a4.setInteractionImage("illustrations", "eochu_bres");
        a4.setFaction("bsg_cylons");

        //system.addRingBand(star, "cat_terrain_rings", "asteroid_belt", 256f, 2, Color.darkGray, 256f, 1050, 45, Terrain.RING, null);
        // Automatic generation of entities after a certain radius to fill a bit the system
        float radiusAfter = StarSystemGenerator.addOrbitingEntities(system, star, StarAge.AVERAGE,
                3, 10, // min/max entities to add
                900, // radius to start adding at
                5, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
                true); // whether to use custom or system-name based names

        system.autogenerateHyperspaceJumpPoints(true, true);

        //Getting rid of some hyperspace nebula, just in case
        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);
    }
}
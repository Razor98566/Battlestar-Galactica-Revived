package data.scripts.campaign.intel.missions;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.FleetEventListener;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.missions.hub.BaseHubMission;
import com.fs.starfarer.api.impl.campaign.missions.hub.HubMissionWithBarEvent;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial;
import com.fs.starfarer.api.impl.campaign.world.TTBlackSite;
import com.fs.starfarer.api.ui.SectorMapAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.List;
import java.util.Map;
import static com.fs.starfarer.api.impl.campaign.ids.FleetTypes.PATROL_MEDIUM;

public class CylonCampaign extends HubMissionWithBarEvent implements FleetEventListener {
    //Days the player has to complete the mission
    public static float MISSION_DAYS = 120f;

    // mission stages
    public static enum Stage {
        HUNT_FIND_CLUE,
        HUNT_KILL_FLEET,
        HUNT_COMPLETED,
        HUNT_FAILED
    }

    // important objects, systems and people
    protected SectorEntityToken derelict;
    protected SectorEntityToken cache;
    protected CampaignFleetAPI target;
    protected PersonAPI executive;
    protected StarSystemAPI system;
    protected StarSystemAPI system2;

    // whether the bar event show should at any given market
    public boolean shouldShowAtMarket(MarketAPI market) {
        return market.getFactionId().equals("bsg_cylons");
    }

    protected boolean create(MarketAPI createdAt, boolean barEvent) {

        // if bar event, let's create a person to actually give it to us
        if (barEvent) {
            setGiverRank(Ranks.SPACE_ADMIRAL);
            setGiverPost(Ranks.POST_BASE_COMMANDER);
            setGiverImportance(PersonImportance.VERY_HIGH);
            setGiverFaction("bsg_cylons");
            setGiverTags(Tags.CONTACT_MILITARY);
            setGiverVoice(Voices.SOLDIER);
            setGiverPortrait("graphics/portraits/Leoben_Commander.png");
            findOrCreateGiver(createdAt, false, false);
        }

        PersonAPI person = getPerson();
        person.setName(new FullName("Leoben", "Connoy", FullName.Gender.MALE));
        person.setPortraitSprite("graphics/portraits/Leoben_Commander.png");

        if (person == null) return false;

        // setting the mission ref allows us to use the Call rulecommand in their dialogues, so that we can make this script do things
        if (!setPersonMissionRef(person, "$bsg_cyl_hunt_ref")) {
            return false;
        }

        // when we complete the mission from a bar event, always allow us to add the questgiver as a contact
        if (barEvent) {
            setGiverIsPotentialContactOnSuccess(1f);
        }

        // set up the disgraced executive
        executive = Global.getSector().getFaction("bsg_colonies").createRandomPerson();
        executive.setPostId(Ranks.POST_FLEET_COMMANDER);
        executive.getMemoryWithoutUpdate().set("$bsg_cyl_hunt_exec", true);
        executive.setName(new FullName("Kendra", "Shaw", FullName.Gender.FEMALE));
        executive.setPortraitSprite("graphics/portraits/Kendra_Shaw.png");


        // pick the system with the clues inside
        requireSystemInterestingAndNotUnsafeOrCore();
        preferSystemInInnerSector();
        preferSystemUnexplored();
        preferSystemInDirectionOfOtherMissions();
        system = pickSystem(true);
        if (system == null) return false;

        // pick the target fleet's system
        requireSystemInterestingAndNotUnsafeOrCore();
        preferSystemWithinRangeOf(system.getLocation(), 3f);
        preferSystemUnexplored();
        requireSystemNot(system);

        system2 = pickSystem(true);
        if (system2 == null) return false;

        // determine the faction and ship type of the derelict
        //String derelict_faction = "bsg_colonies";
        //DerelictShipEntityPlugin.DerelictType derelict_type = DerelictShipEntityPlugin.DerelictType.LARGE;

        // spawn a supply cache and derelict ship, both serving as clues. They have memory flags that are checked for in rules.csv
        cache = spawnEntity(Entities.SUPPLY_CACHE, new BaseHubMission.LocData(BaseHubMission.EntityLocationType.HIDDEN, null, system));
        derelict = TTBlackSite.addDerelict(system, system.getCenter(), "bsg_arachne_variant", "Parker", "1", ShipRecoverySpecial.ShipCondition.AVERAGE, 2500f, true);
        //derelict = spawnDerelict(derelict_faction, derelict_type, new BaseHubMission.LocData(BaseHubMission.EntityLocationType.HIDDEN, null, system));
        cache.getMemoryWithoutUpdate().set("$bsg_cyl_hunt_clue", true);
        setEntityMissionRef(cache, "$bsg_cyl_hunt_ref");
        derelict.getMemoryWithoutUpdate().set("$bsg_cyl_hunt_clue", true);
        setEntityMissionRef(derelict, "$bsg_cyl_hunt_ref");


        // set up the target fleet. I've done this using the old style, because the trigger-system doesn't support event listeners by default,
        // and we need to know when this fleet dies or despawns
        FleetParamsV3 params = new FleetParamsV3(
                null,
                null,
                "bsg_colonies",
                null,
                PATROL_MEDIUM,
                0f, // combatPts
                0f, // freighterPts
                0f, // tankerPts
                0f, // transportPts
                0f, // linerPts
                0f, // utilityPts
                0 // qualityMod
        );

        //FleetParamsV3 params = new FleetParamsV3(null, "bsg_colonies", null, null, 120, 10, 10, 10, 10, 10, 0);
        //ShipAPI ship1 = Global.getFactory().createFleetMember(FleetMemberType.SHIP, "bsg_mercury_variant");
        //ShipVariantAPI shipVariant2 = Global.getSettings().getVariant("bsg_berserk_variant");
        //ShipVariantAPI shipVariant3 = Global.getSettings().getVariant("bsg_berserk_variant");
        //ShipVariantAPI shipVariant4 = Global.getSettings().getVariant("bsg_cygnus_variant");


        //fleetMember = Global.getFactory().createEmptyFleet("bsg_colonies", "Hmmm", true);
        //fleetMember.isCapital();
        //fleetMember = Global.getFactory().createFleetMember(FleetMemberType.SHIP, "bsg_berserk_variant");
        //fleetMember = Global.getFactory().createFleetMember(FleetMemberType.SHIP, "bsg_berserk_variant");
        //fleetMember = Global.getFactory().createFleetMember(FleetMemberType.SHIP, "bsg_cygnus_variant");

        // toughen them up, in exchange for the shoddy ship quality we set
        params.averageSMods = 2;
        target = FleetFactoryV3.createFleet(params);

        target.getFleetData().addFleetMember("bsg_mercury_variant").setFlagship(true);
        target.getFleetData().addFleetMember("bsg_berserk_variant");
        target.getFleetData().addFleetMember("bsg_cygnus_variant");
        target.getFleetData().addFleetMember("bsg_berserk_variant");
        target.setName(executive.getNameString() + "'s Strike Group");
        target.setNoFactionInName(true);
        target.setCommander(executive);
        target.getFlagship().setCaptain(executive);
        target.getFlagship().setShipName("Peragus I");

        FleetMemberAPI bsg_mercury = target.getFleetData().getFleet().getFlagship();

        bsg_mercury.getVariant().addTag(Tags.VARIANT_ALWAYS_RECOVERABLE);

        Misc.makeHostile(target);
        Misc.makeNoRepImpact(target, "$bsg_cyl_hunt");
        Misc.makeImportant(target, "$bsg_cyl_hunt");

        target.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_MAKE_AGGRESSIVE, "$bsg_cyl_hunt");
        target.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_MAKE_AGGRESSIVE_ONE_BATTLE_ONLY, "$bsg_cyl_hunt");
        target.getMemoryWithoutUpdate().set(MemFlags.FLEET_IGNORED_BY_OTHER_FLEETS, "$bsg_cyl_hunt");
        target.getMemoryWithoutUpdate().set(MemFlags.FLEET_IGNORES_OTHER_FLEETS, "$bsg_cyl_hunt");
        target.getMemoryWithoutUpdate().set("$bsg_cyl_hunt_execfleet", true);
        target.getAI().addAssignment(FleetAssignment.PATROL_SYSTEM, system2.getCenter(), 200f, null);
        target.addEventListener(this);
        system2.addEntity(target);

/*        // optionally spawn a Remnant fleet when we close in on the executive's system
        if (remnant) {
            beginWithinHyperspaceRangeTrigger(system2,1f, false, Stage.FIND_CLUE, Stage.KILL_FLEET);
            triggerCreateFleet(FleetSize.LARGE, FleetQuality.VERY_HIGH, Factions.REMNANTS, PATROL_MEDIUM, system2);
            triggerMakeHostileAndAggressive();
            triggerAutoAdjustFleetStrengthMajor();
            triggerSetRemnantConfig();
            //triggerMakeFleetIgnoredByOtherFleets();
            triggerPickLocationAtInSystemJumpPoint(system2);
            triggerSpawnFleetAtPickedLocation(null, null);
            triggerOrderFleetPatrol(system2, true, Tags.JUMP_POINT, Tags.SALVAGEABLE, Tags.PLANET);
            endTrigger();
        }*/

        // set a global reference we can use, useful for once-off missions.
        if (!setGlobalReference("$bsg_cyl_hunt_ref")) return false;

        // set our starting, success and failure stages
        setStartingStage(CylonCampaign.Stage.HUNT_FIND_CLUE);
        setSuccessStage(CylonCampaign.Stage.HUNT_COMPLETED);
        setFailureStage(CylonCampaign.Stage.HUNT_FAILED);

        // set stage transitions when certain global flags are set, and when certain flags are set on the questgiver
        setStageOnGlobalFlag(CylonCampaign.Stage.HUNT_KILL_FLEET, "$bsg_cyl_hunt_foundclue");
        setStageOnMemoryFlag(CylonCampaign.Stage.HUNT_COMPLETED, person, "$bsg_cyl_hunt_completed");
        setStageOnMemoryFlag(CylonCampaign.Stage.HUNT_FAILED, person, "$bsg_cyl_hunt_failed" );
        // set time limit and credit reward
        setTimeLimit(CylonCampaign.Stage.HUNT_FAILED, MISSION_DAYS, system2);
        setCreditReward(BaseHubMission.CreditReward.VERY_HIGH);

        return true;
    }

    // when Call-ing something that isn't a default option for a mission, it'll try and run this method with "action" being the first parameter
    // e.g Call $global.bsg_col_hunt_ref unsetClues
    @Override
    protected boolean callAction(String action, String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        if (action.equals("unsetClues")){
            // make the other clue no longer a clue
            if (cache != null) {
                cache.getMemoryWithoutUpdate().unset("$bsg_cyl_hunt_clue");
            }
            if (derelict != null) {
                derelict.getMemoryWithoutUpdate().unset("$bsg_cyl_hunt_clue");
            }
            return true;
        }
        return false;
    }

    // during the initial dialogue and in any dialogue where we use "Call $bsg_cyl_hunt_ref updateData", these values will be put in memory
    // here, used so we can, say, type $bsg_col_hunt_execName and automatically insert the disgraced executive's name
    protected void updateInteractionDataImpl() {
        set("$bsg_cyl_hunt_barEvent", isBarEvent());
        set("$bsg_cyl_hunt_manOrWoman", getPerson().getManOrWoman());
        set("$bsg_cyl_hunt_heOrShe", getPerson().getHeOrShe());
        set("$bsg_cyl_hunt_reward", Misc.getWithDGS(getCreditsReward()));

        set("$bsg_cyl_hunt_personName", getPerson().getNameString());
        set("$bsg_cyl_hunt_execName", executive.getNameString());
        set("$bsg_cyl_hunt_systemName", system.getNameWithLowercaseTypeShort());
        set("$bsg_cyl_hunt_system2Name", system2.getNameWithLowercaseTypeShort());
        set("$bsg_cyl_hunt_dist", getDistanceLY(system));
    }

    // used to detect when the executive's fleet is destroyed and complete the mission
    public void reportBattleOccurred(CampaignFleetAPI fleet, CampaignFleetAPI primaryWinner, BattleAPI battle) {
        Global.getSector().getPlayerFleet().getFleetData().addFleetMember("bsg_argos_variant");
        if (isDone() || result != null) return;

        // also credit the player if they're in the same location as the fleet and nearby
        float distToPlayer = Misc.getDistance(fleet, Global.getSector().getPlayerFleet());
        boolean playerInvolved = battle.isPlayerInvolved() || (fleet.isInCurrentLocation() && distToPlayer < 2000f);

        if (battle.isInvolved(fleet) && !playerInvolved) {
            if (fleet.getFlagship() == null || fleet.getFlagship().getCaptain() != target) {
                fleet.setCommander(fleet.getFaction().createRandomPerson());
                getPerson().getMemoryWithoutUpdate().set("$bsg_cyl_hunt_completed", true);
                return;
            }
        }

        if (!playerInvolved || !battle.isInvolved(fleet) || battle.onPlayerSide(fleet)) {
            return;
        }

        // didn't destroy the original flagship
        if (fleet.getFlagship() != null && fleet.getFlagship().getCaptain() == target) return;

        getPerson().getMemoryWithoutUpdate().set("$bsg_cyl_hunt_completed", true);

    }

    // if the fleet despawns for whatever reason, fail the mission
    public void reportFleetDespawnedToListener(CampaignFleetAPI fleet, CampaignEventListener.FleetDespawnReason reason, Object param) {
        if (isDone() || result != null) return;

        if (fleet.getMemoryWithoutUpdate().contains("$bsg_cyl_hunt_execfleet")) {
            System.err.println("Something fucked up has happened and the quest fleet for the quest: " + getBaseName() + " has disappeared");
            getPerson().getMemoryWithoutUpdate().set("$bsg_cyl_hunt_failed", true);
        }
    }

    // description when selected in intel screen
    @Override
    public void addDescriptionForNonEndStage(TooltipMakerAPI info, float width, float height) {
        float opad = 10f;
        Color h = Misc.getHighlightColor();
        if (currentStage == CylonCampaign.Stage.HUNT_FIND_CLUE) {
            info.addPara("Look for clues as to the Colonial Fleet's location in the " +
                    system.getNameWithLowercaseTypeShort() + ".", opad);
        } else if (currentStage == CylonCampaign.Stage.HUNT_KILL_FLEET) {
            info.addPara("Hunt down and eliminate Kendra Shaw's fleet in the " +
                    system2.getNameWithLowercaseTypeShort() + ".", opad);
        }
        if (isDevMode()) {
            info.addPara("DEVMODE: KENDRA IS LOCATED IN THE " +
                    system2.getNameWithLowercaseTypeShort() + ".", opad);
        }
    }

    // short description in popups and the intel entry
    @Override
    public boolean addNextStepText(TooltipMakerAPI info, Color tc, float pad) {
        Color h = Misc.getHighlightColor();
        if (currentStage == CylonCampaign.Stage.HUNT_FIND_CLUE) {
            info.addPara("Look for clues in the " +
                    system.getNameWithLowercaseTypeShort(), tc, pad);
            return true;
        } else if (currentStage == CylonCampaign.Stage.HUNT_KILL_FLEET) {
            info.addPara("Hunt down Kendra in the " +
                    system2.getNameWithLowercaseTypeShort(), tc, pad);
            return true;
        }
        return false;
    }

    // where on the map the intel screen tells us to go
    @Override
    public SectorEntityToken getMapLocation(SectorMapAPI map) {
        if (currentStage == CylonCampaign.Stage.HUNT_FIND_CLUE) {
            return getMapLocationFor(system.getCenter());
        } else if (currentStage == CylonCampaign.Stage.HUNT_KILL_FLEET) {
            return getMapLocationFor(system2.getCenter());
        }
        return null;
    }

    // mission name
    @Override
    public String getBaseName() {
        return "Hunt down the Colonial";
    }
}

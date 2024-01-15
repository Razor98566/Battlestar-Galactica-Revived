package data.scripts.campaign.intel.missions;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.FleetEventListener;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.missions.hub.BaseHubMission;
import com.fs.starfarer.api.impl.campaign.missions.hub.HubMissionWithBarEvent;
import com.fs.starfarer.api.ui.SectorMapAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.*;
import java.util.List;
import java.util.Map;
import static com.fs.starfarer.api.impl.campaign.ids.FleetTypes.PATROL_MEDIUM;

public class ColonialCampaign extends HubMissionWithBarEvent implements FleetEventListener {

    //Days the player has to complete the mission
    public static float MISSION_DAYS = 120f;

    // mission stages
    public static enum Stage {
        HUNT_FIND_CLUE,
        HUNT_KILL_FLEET,
        HUNT_COMPLETED,
        HUNT_FAILED,
        GATHER_COLLECTION_PHASE,
        GATHER_COMPLETED,
        HUNT2_KILL_FLEET,
        HUNT2_COMPLETED,
        DEFEND_KILL_FLEET,
        DEFEND_COMPLETED,
        SEARCH_FIND_CLUE,
        SEARCH_KILL_REMNANTS,
        SEARCH_COMPLETED,
        HUB_LOCATE_HUB,
        HUB_KILL_HUB,
        HUB_COMPLETED,
        QUEST_LINE_COMPLETED
    }

    // important objects, systems and people
    protected SectorEntityToken hunt_derelict;
    protected SectorEntityToken hunt_cache;
    protected CampaignFleetAPI hunt_target;
    protected PersonAPI executive;
    protected StarSystemAPI hunt_system;
    protected StarSystemAPI hunt_system2;

    // whether the bar event show should at any given market
    public boolean shouldShowAtMarket(MarketAPI market) {
        return market.getFactionId().equals("bsg_colonies");
    }

    protected boolean create(MarketAPI createdAt, boolean barEvent) {

        // if bar event, let's create a person to actually give it to us
        if (barEvent) {
            setGiverRank(Ranks.SPACE_ADMIRAL);
            setGiverPost(Ranks.POST_BASE_COMMANDER);
            setGiverImportance(PersonImportance.VERY_HIGH);
            setGiverFaction("bsg_colonies");
            setGiverTags(Tags.CONTACT_MILITARY);
            setGiverVoice(Voices.SOLDIER);
            setGiverPortrait("");
            findOrCreateGiver(createdAt, false, false);
        }

        PersonAPI person = getPerson();
        if (person == null) return false;

        // setting the mission ref allows us to use the Call rulecommand in their dialogues, so that we can make this script do things
        if (!setPersonMissionRef(person, "$bsg_col_hunt_ref")) {
            return false;
        }

        // when we complete the mission from a bar event, always allow us to add the questgiver as a contact
        if (barEvent) {
            setGiverIsPotentialContactOnSuccess(1f);
        }

        // set up the disgraced executive
        executive = Global.getSector().getFaction("bsg_cylons").createRandomPerson();
        executive.setRankId(Ranks.CLONE);
        executive.setPostId(Ranks.POST_FLEET_COMMANDER);
        executive.getMemoryWithoutUpdate().set("$bsg_col_hunt_exec", true);
        executive.setName(new FullName("Leoben", "Conoy", FullName.Gender.MALE));
        executive.setPortraitSprite("C:\\Program Files (x86)\\Fractal Softworks\\Starsector 0.96a\\Starsector\\mods\\Battlestar Galactica Revived\\graphics\\Cylon\\Leoben_Commander.png");


        // pick the system with the clues inside
        requireSystemInterestingAndNotUnsafeOrCore();
        preferSystemInInnerSector();
        preferSystemUnexplored();
        preferSystemInDirectionOfOtherMissions();

        hunt_system = pickSystem(true);
        if (hunt_system == null) return false;

        // pick the target fleet's system
        requireSystemInterestingAndNotUnsafeOrCore();
        preferSystemWithinRangeOf(hunt_system.getLocation(), 3f);
        preferSystemUnexplored();
        requireSystemNot(hunt_system);

        hunt_system2 = pickSystem(true);
        if (hunt_system2 == null) return false;

        // determine the faction and ship type of the derelict
        String derelict_faction = "bsg_cylons";
        DerelictShipEntityPlugin.DerelictType derelict_type = DerelictShipEntityPlugin.DerelictType.LARGE;


        // spawn a supply cache and derelict ship, both serving as clues. They have memory flags that are checked for in rules.csv
        hunt_cache = spawnEntity(Entities.SUPPLY_CACHE, new BaseHubMission.LocData(BaseHubMission.EntityLocationType.HIDDEN, null, hunt_system));
        hunt_derelict = spawnDerelict(derelict_faction, derelict_type, new BaseHubMission.LocData(BaseHubMission.EntityLocationType.HIDDEN, null, hunt_system));
        hunt_cache.getMemoryWithoutUpdate().set("$bsg_col_hunt_clue", true);
        setEntityMissionRef(hunt_cache, "$bsg_col_hunt_ref");
        hunt_derelict.getMemoryWithoutUpdate().set("$bsg_col_hunt_clue", true);
        setEntityMissionRef(hunt_derelict, "$bsg_col_hunt_ref");

        // set up the target fleet. I've done this using the old style, because the trigger-system doesn't support event listeners by default,
        // and we need to know when this fleet dies or despawns
        FleetParamsV3 params = new FleetParamsV3(
                null,
                null,
                "bsg_cylons",
                null,
                PATROL_MEDIUM,
                120f, // combatPts
                10f, // freighterPts
                10f, // tankerPts
                10f, // transportPts
                10f, // linerPts
                10f, // utilityPts
                0 // qualityMod
        );
        // toughen them up, in exchange for the shoddy ship quality we set
        params.averageSMods = 2;
        hunt_target = FleetFactoryV3.createFleet(params);

        hunt_target.setName(executive.getNameString() + "'s Rogue fleet");
        hunt_target.setNoFactionInName(true);

        hunt_target.setCommander(executive);
        hunt_target.getFlagship().setCaptain(executive);

        Misc.makeHostile(hunt_target);
        Misc.makeNoRepImpact(hunt_target, "$bsg_col_hunt");
        Misc.makeImportant(hunt_target, "$bsg_col_hunt");

        hunt_target.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_MAKE_AGGRESSIVE, "$bsg_col_hunt");
        hunt_target.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_MAKE_AGGRESSIVE_ONE_BATTLE_ONLY, "$bsg_col_hunt");
        hunt_target.getMemoryWithoutUpdate().set(MemFlags.FLEET_IGNORED_BY_OTHER_FLEETS, "$bsg_col_hunt");
        hunt_target.getMemoryWithoutUpdate().set(MemFlags.FLEET_IGNORES_OTHER_FLEETS, "$bsg_col_hunt");
        hunt_target.getMemoryWithoutUpdate().set("$bsg_col_hunt_execfleet", true);
        hunt_target.getAI().addAssignment(FleetAssignment.PATROL_SYSTEM, hunt_system2.getCenter(), 200f, null);
        hunt_target.addEventListener(this);
        hunt_system2.addEntity(hunt_target);

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
        if (!setGlobalReference("$bsg_col_hunt_ref")) return false;

        // set our starting, success and failure stages
        setStartingStage(Stage.HUNT_FIND_CLUE);
        setSuccessStage(Stage.QUEST_LINE_COMPLETED);
        setFailureStage(Stage.HUNT_FAILED);

        // set stage transitions when certain global flags are set, and when certain flags are set on the questgiver
        setStageOnGlobalFlag(Stage.HUNT_KILL_FLEET, "$bsg_col_hunt_foundclue");
        setStageOnMemoryFlag(Stage.HUNT_COMPLETED, person, "$bsg_col_hunt_completed");
        setStageOnMemoryFlag(Stage.HUNT_FAILED, person, "$bsg_col_hunt_failed" );
        // set time limit and credit reward
        setTimeLimit(Stage.HUNT_FAILED, MISSION_DAYS, hunt_system2);
        setCreditReward(CreditReward.VERY_HIGH);

        return true;
    }

    // when Call-ing something that isn't a default option for a mission, it'll try and run this method with "action" being the first parameter
    // e.g Call $global.bsg_col_hunt_ref unsetClues
    @Override
    protected boolean callAction(String action, String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        if (action.equals("unsetClues")){
            // make the other clue no longer a clue
            if (hunt_cache != null) {
                hunt_cache.getMemoryWithoutUpdate().unset("$bsg_col_hunt_clue");
            }
            if (hunt_derelict != null) {
                hunt_derelict.getMemoryWithoutUpdate().unset("$bsg_col_hunt_clue");
            }
            return true;
        }
        return false;
    }

    // during the initial dialogue and in any dialogue where we use "Call $bsg_col_hunt_ref updateData", these values will be put in memory
    // here, used so we can, say, type $bsg_col_hunt_execName and automatically insert the disgraced executive's name
    protected void updateInteractionDataImpl() {
        set("$bsg_col_hunt_barEvent", isBarEvent());
        set("$bsg_col_hunt_manOrWoman", getPerson().getManOrWoman());
        set("$bsg_col_hunt_heOrShe", getPerson().getHeOrShe());
        set("$bsg_col_hunt_reward", Misc.getWithDGS(getCreditsReward()));

        set("$bsg_col_hunt_personName", getPerson().getNameString());
        set("$bsg_col_hunt_execName", executive.getNameString());
        set("$bsg_col_hunt_systemName", hunt_system.getNameWithLowercaseTypeShort());
        set("$bsg_col_hunt_system2Name", hunt_system2.getNameWithLowercaseTypeShort());
        set("$bsg_col_hunt_dist", getDistanceLY(hunt_system));
    }

    // used to detect when the executive's fleet is destroyed and complete the mission
    public void reportBattleOccurred(CampaignFleetAPI fleet, CampaignFleetAPI primaryWinner, BattleAPI battle) {
        if (isDone() || result != null) return;

        // also credit the player if they're in the same location as the fleet and nearby
        float distToPlayer = Misc.getDistance(fleet, Global.getSector().getPlayerFleet());
        boolean playerInvolved = battle.isPlayerInvolved() || (fleet.isInCurrentLocation() && distToPlayer < 2000f);

        if (battle.isInvolved(fleet) && !playerInvolved) {
            if (fleet.getFlagship() == null || fleet.getFlagship().getCaptain() != hunt_target) {
                fleet.setCommander(fleet.getFaction().createRandomPerson());
                getPerson().getMemoryWithoutUpdate().set("$bsg_col_hunt_completed", true);
                return;
            }
        }

        if (!playerInvolved || !battle.isInvolved(fleet) || battle.onPlayerSide(fleet)) {
            return;
        }

        // didn't destroy the original flagship
        if (fleet.getFlagship() != null && fleet.getFlagship().getCaptain() == hunt_target) return;

        getPerson().getMemoryWithoutUpdate().set("$bsg_col_hunt_completed", true);

    }

    // if the fleet despawns for whatever reason, fail the mission
    public void reportFleetDespawnedToListener(CampaignFleetAPI fleet, CampaignEventListener.FleetDespawnReason reason, Object param) {
        if (isDone() || result != null) return;

        if (fleet.getMemoryWithoutUpdate().contains("$bsg_col_hunt_execfleet")) {
            getPerson().getMemoryWithoutUpdate().set("$bsg_col_hunt_failed", true);
        }
    }

    // description when selected in intel screen
    @Override
    public void addDescriptionForNonEndStage(TooltipMakerAPI info, float width, float height) {
        float opad = 10f;
        Color h = Misc.getHighlightColor();
        if (currentStage == Stage.HUNT_FIND_CLUE) {
            info.addPara("Look for clues as to the Cylon fleet location in the " +
                    hunt_system.getNameWithLowercaseTypeShort() + ".", opad);
        } else if (currentStage == Stage.HUNT_KILL_FLEET) {
            info.addPara("Hunt down and eliminate Leoben's fleet in the " +
                    hunt_system2.getNameWithLowercaseTypeShort() + ".", opad);
        }
        if (isDevMode()) {
            info.addPara("DEVMODE: LEOBEN IS LOCATED IN THE " +
                    hunt_system2.getNameWithLowercaseTypeShort() + ".", opad);
        }
    }

    // short description in popups and the intel entry
    @Override
    public boolean addNextStepText(TooltipMakerAPI info, Color tc, float pad) {
        Color h = Misc.getHighlightColor();
        if (currentStage == Stage.HUNT_FIND_CLUE) {
            info.addPara("Look for clues in the " +
                    hunt_system.getNameWithLowercaseTypeShort(), tc, pad);
            return true;
        } else if (currentStage == Stage.HUNT_KILL_FLEET) {
            info.addPara("Hunt down Leoben in the " +
                    hunt_system2.getNameWithLowercaseTypeShort(), tc, pad);
            return true;
        }
        return false;
    }

    // where on the map the intel screen tells us to go
    @Override
    public SectorEntityToken getMapLocation(SectorMapAPI map) {
        if (currentStage == Stage.HUNT_FIND_CLUE) {
            return getMapLocationFor(hunt_system.getCenter());
        } else if (currentStage == Stage.HUNT_KILL_FLEET) {
            return getMapLocationFor(hunt_system2.getCenter());
        }
        return null;
    }

    // mission name
    @Override
    public String getBaseName() {
        return "Hunt down the Cylon";
    }

}

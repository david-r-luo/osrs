package core;

import org.tbot.bot.TBot;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptController;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Quests;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.Area;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.Tile;

@Manifest(name = "Script Controller", authors = "me")
public class MainHandler extends ScriptController {
//    Area tutArea = new Area(3091, 3102, 3096, 3110);
//    private static final String CHICKEN_LUMBRIDGE_KEY = "profile key here";

    @Override
    public boolean onStart() {
        // Start the LOCAL script AIOCombat, with the arguments
        // 'CHICKEN_LUMBRIDGE_KEY' (I use profiles in my combat script)
        log("Starting Script");
        runScript(399);
//        runScript(399); //Tutorial
//        if (getCurrentScript().getManifest().name().equals())
//        Time.sleepUntil(new Condition() {
//            @Override
//            public boolean check() {
//                return !TBot.getBot().getScriptHandler().getScript().isRunning();
//            }
//        });
//
//        runScript(421); //Cook's Assistant
//        Time.sleep(1204, 3024);
//        runScript(new LootHide());
//        Time.sleep(1204, 3024);
//        runScript(new TrainCombat()); //COMBAT
//        Time.sleep(1204, 3024);

//        runScript(); //Draynor fishing
//        Time.sleep(1204, 3024);
//        runScript(); //COoking in lum
//        Time.sleep(1204, 3024);
//        runScript(); //SHeep shearer
//        Time.sleep(1204, 3024);
//        runScript(); //Woodcut
//        Time.sleep(1204, 3024);
//        runScript(); //Mining swamp/varrock
//        Time.sleep(1204, 3024);
//        runScript(); //Romeo and Juliet

//        Time.sleep(1204, 3024);
//        runScript(new SellHide());
//        Time.sleep(1204, 3024);
//        runScript(new BuyJugs());
//        Time.sleep(1204, 3024);
//        runScript(432);
//        Time.sleep(1204, 3024);


        return true;
    }

    @Override
    public void onFinishScript() {
        log("Moving onto next script!");
    }

    @Override
    public void onScriptLoop(int lastLoopReturn) {
        AntiBan.checkTimers();
        log("Current Script: " + getCurrentScript().getManifest().name());

        if (Settings.get(281) >= 1000 && getCurrentScript().getManifest().name().equals("Tutorial ISland")) {
            runScript(421);
        } else if (Quests.isCompleted("Cook's Assistant") && Bank.getCount("Cowhide") < 80
                && getCurrentScript().getManifest().name().equals("Cook's Assistant Assistant")) {
            runScript(new LootHide());
        } else if (Bank.getCount("Cowhide") > 80 && getCurrentScript().getManifest().name().equals("LootHide")) {
            runScript(new TrainCombat());
        } else if (Players.getLocal().getCombatLevel() >= 15) {
            log("Run Draynor");


//        } else if () {
//
//        } else if () {
//
        } else {
            log("wat");
//
        }
    }

//    static int getCurrentFloor(){
//        String location = Players.getLocal().getLocation().toString();
//        String floor = location.substring(location.length()-2,location.length()-1);
//        return Integer.valueOf(floor);
//    }
//
//    public void bank() {
//        Tile stairTile = new Tile(3206, 3208);
//        Path pathToStairs = Walking.findPath(stairTile);
//        if (pathToStairs != null) {
//            pathToStairs.traverse();
//            Time.sleepUntil(new Condition() {
//                @Override
//                public boolean check() {
//                    return stairTile.distance() < 3;
//                }
//            }, Random.nextInt(500, 800));
//        }
//        GameObject stairs = GameObjects.getNearest("Staircase");
//        stairs.interact("Climb-up");
//        Time.sleep(1400, 1900);
//        GameObject stairs2 = GameObjects.getNearest("Staircase");
//        stairs2.interact("Climb-up");
//        Time.sleep(1600, 2100);
//
//    }

//    public State getState() {
//        if (Settings.get(281) < 1000) {
//            return State.TUTORIAL;
//        } else if (Settings.get(281) >= 1000 && getCurrentScript().getManifest().name().equals("Tutorial Island")) {
//            return State.COOK;
//        } else if (Quests.isCompleted("Cook's Assistant") && Bank.getCount("Cowhide") < 80) {
//            return State.LOOTHIDE;
//                    COOK,
//                    COMBAT,
//                    FISHING,
//                    COOKING,
//                    SHEEP,
//                    WC,
//                    MINING,
//                    ROMJU,
//                    SELLHIDE,
//                    BUYJUGS,
//                    JUGFILL,
//                    BUYPACKS
//        }
//        return null;
//    }
}


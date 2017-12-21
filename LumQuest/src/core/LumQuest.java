package core;

import org.tbot.methods.*;
import org.tbot.methods.input.keyboard.Keyboard;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.tabs.Quests;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.NPC;
import org.tbot.wrappers.Tile;
import states.State;

/**
 * Created by David on 1/17/2017.
 */
public class LumQuest {

    public void LumQuest(State state) {

        if (state == State.BANKCOOK) {


            Tile cookTile = new Tile(3300, 3300); //TODO FIND REAL COOK TILE
            Path pathToCook = Walking.findPath(cookTile);

            if (Inventory.getUniqueItems().size() > 2) {
                deposit1();
            }
//            GameObject cook = GameObjects.getNearest("Cook");
            NPC cook1 = Npcs.getNearest("Cook");
            cook1.interact("Talk-to");
            Keyboard.pressKey(' ');
            Time.sleep(710, 1052);
            Keyboard.pressKey('1');
            Time.sleep(710, 1052);
            Keyboard.pressKey(' ', 3);
            Time.sleep(710, 1052);
            Keyboard.pressKey('1');
            Time.sleep(710, 1052);
            Keyboard.pressKey(' ');
            Time.sleep(710, 1052);
            Keyboard.pressKey('4');
            Time.sleep(710, 1052);
            Keyboard.pressKey(' ');
            Time.sleep(710, 1052);

        } else if (state == State.MILK) {
            Tile dairyTile = new Tile(3300, 3300); //TODO FIND REAL COW TILE
            Path pathToCow = Walking.findPath(dairyTile);
            pathToCow.traverse();
            if (pathToCow != null) {
                pathToCow.traverse();
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return dairyTile.distance() < 4;
                    }
                }, Random.nextInt(700, 1400));
            }
            Time.sleep(183,643);
            GameObject cow = GameObjects.getNearest("Dairy cow");
            cow.interact("Milk");
            Time.sleep(210, 432);

        } else if (state == State.EGG) {
            Tile eggTile = new Tile(3300, 3300); //TODO FIND REAL EGG TILE
            Path pathToEgg = Walking.findPath(eggTile);
            if (pathToEgg != null) {
                pathToEgg.traverse();
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return eggTile.distance() < 4;
                    }
                }, Random.nextInt(700, 1400));
            }
            Time.sleep(183,643);
            GameObject egg = GameObjects.getNearest("Egg");
            egg.interact("Take");
            Time.sleep(183,543);

        } else if (state == State.FLOUR) {
            Tile wheatTile = new Tile(3300, 3300); //TODO FIND REAL FLOUR TILE
            Path pathToWheat = Walking.findPath(wheatTile);
            if (pathToWheat != null) {
                pathToWheat.traverse();
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return wheatTile.distance() < 4;
                    }
                }, Random.nextInt(700, 1400));
            }
            Time.sleep(183,643);
            GameObject wheat = GameObjects.getNearest("Wheat");
            wheat.interact("Take");
            Time.sleep(183,543);
            Tile hopperTile = new Tile(3300, 3300); //TODO FIND REAL FLOUR TILE
            Path pathToHopper = Walking.findPath(hopperTile);
            //TODO UP STAIRS?
            Inventory.useItemOn("Wheat", GameObjects.getNearest("Hopper"));
//            Inventory.getFirst("Wheat").interact("Use");
//            GameObject hopper = GameObjects.getNearest("Hopper");
            GameObject lever = GameObjects.getNearest("Lever");
            lever.interact("Pull");
            //go downstairs and get flour



        } else if (state == State.FRED) {
            Tile fredTile = new Tile(3300, 3300); //TODO FIND REAL FLOUR TILE
            Path pathToFred = Walking.findPath(fredTile);
            if (pathToFred != null) {
                pathToFred.traverse();
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return fredTile.distance() < 4;
                    }
                }, Random.nextInt(700, 1400));
            }
            GameObject shears = GameObjects.getNearest("Shears");

        } else if (state == State.SHEARING) {
            GameObject sheep = GameObjects.getNearest("Sheep");
            sheep.interact("Shear");

        } else if (state == State.CASTLE) {

        } else if (state == State.SPIN) {

        } else if (state == State.FRED2) {


        }

    }

    public State getSubstate() {
        if(Quests.getQuestProgress("Cook's Assistant") != Quests.Progress.IN_PROGRESS) {
            return State.BANKCOOK;
        } else if (!Inventory.contains("Bucket of milk")) {
            return State.MILK;
        } else if (!Inventory.contains("Egg")) {
            return State.EGG;
        } else if (!Inventory.contains("Pot of flour")) {
            return State.FLOUR;
        } else if (Quests.getQuestProgress("Sheep Shearer") != Quests.Progress.IN_PROGRESS && !Inventory.contains("Shears")) {
            return State.FRED;
        } else if (Inventory.getCount("Wool") < 20) {
            return State.SHEARING;
        } else if (Quests.getQuestProgress("Cook's Assistant") == Quests.Progress.IN_PROGRESS) {
            return State.CASTLE;
        } else if (Inventory.getCount("Ball of wool") < 20) {
            return State.SPIN;
        } else if (Quests.getQuestProgress("Sheep Shearer") != Quests.Progress.IN_PROGRESS && Inventory.getCount("Ball of wool") >= 20) {
            return State.FRED2;
        }
        return null;
    }

    public void deposit1() {
        Tile bankTile = new Tile(); //TODO FIND REAL BANK TILE
        Path pathToBank = Walking.findPath(bankTile);
        if (pathToBank != null) {
            pathToBank.traverse();
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return bankTile.distance() < 3;
                }
            }, Random.nextInt(700, 1300));
        }
        GameObject booth = GameObjects.getNearest("Bank booth"); //TODO CHECK NAME

        booth.interact("Bank");
        Time.sleep(800, 1100);
        Bank.depositAll();
        Time.sleep(800, 1100);
        Bank.withdraw("Pot", 1);
        Time.sleep(600, 780);
        Bank.withdraw("Bucket", 1);
        Time.sleep(600, 780);
        Bank.close();
    }


    //walk to bank
    //deposit all but bucket of milk and pot
    //talk to cook to start quest
    //milk cow
    //get egg
    //pick grain and get flour
    //talk to fred
    //shear sheep
    //go back to castle
    //finish cooks quest
    //spin wool
    //pick up iron man armor
    //go to fred finish quest


}

package core;

import org.tbot.bot.TBot;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.Area;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.NPC;
import org.tbot.wrappers.Tile;

/**
 * Created by David on 1/21/2017.
 */
@Manifest(name = "LootHide", authors = "me")
public class LootHide extends AbstractScript{
    Area cowArea = new Area(new Tile[]{new Tile(3252, 3254), new Tile(3252, 3271), new Tile(3240, 3283), new Tile(3240, 3299), new Tile(3266, 3299)});
//    Area bankArea = new Area();
    Tile cowTile = new Tile(3256, 3265);
//    Tile bankTile = new Tile();

    public boolean onStart() {
        log("Started Script");
        return true;
    }

    @Override
    public int loop() {
        if (Bank.getCount("Cowhide") >= 80 || Bank.getCount("Jug") > 200 || Bank.getCount("Coins") >= 5000) {
            TBot.getBot().getScriptHandler().stopScript();
        } else if (Inventory.isFull()) {
            Tile bankTile = new Tile(3208, 3220, 2);
            Path pathToBank = Walking.findPath(bankTile);
            if (pathToBank != null) {
                pathToBank.traverse();
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return bankTile.distance() < 3;
                    }
                }, Random.nextInt(400, 800));
            }

            NPC banker = Npcs.getNearest("Banker");
            banker.interact("Bank");
            Time.sleep(500, 800);
            Bank.depositAll();
            Time.sleep(700, 900);
            Bank.close();

//            Tile stairTile = new Tile(3206, 3208);
//            Path pathToStairs = Walking.findPath(stairTile);
//            if (pathToStairs != null) {
//                pathToStairs.traverse();
//                Time.sleepUntil(new Condition() {
//                    @Override
//                    public boolean check() {
//                        return stairTile.distance() < 3;
//                    }
//                }, Random.nextInt(400, 800));
//            }
//
//            GameObject stairs = GameObjects.getNearest("Staircase");
//            stairs.interact("Climb-up");
//            Time.sleep(1000, 1200);
//            GameObject stairs2 = GameObjects.getNearest("Staircase");
//            if (stairs2.hasAction("Climb-up")) {
//                stairs2.interact("Climb-up");
//            }
//            Time.sleep(800, 1200);
//            if (getCurrentFloor() == 2) {
//                Tile bankTile = new Tile(3208, 3220, 2);
//                Path pathToBank = Walking.findPath(bankTile);
//                if (pathToBank != null) {
//                    pathToBank.traverse();
//                    Time.sleepUntil(new Condition() {
//                        @Override
//                        public boolean check() {
//                            return bankTile.distance() < 3;
//                        }
//                    }, Random.nextInt(400, 800));
//                }
//
//                NPC banker = Npcs.getNearest("Banker");
//                banker.interact("Bank");
//                Time.sleep(500, 800);
//                Bank.depositAll();
//                Time.sleep(700, 900);
//                Bank.close();

            //run to bank

        } else if (!Inventory.isFull() && !cowArea.contains(Players.getLocal())) {
//            walk to cow
            Path cowPath = Walking.findPath(cowTile);
            if (cowPath != null) {
                cowPath.traverse();
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return cowTile.distance() < 4;
                    }
                }, Random.nextInt(500, 900));
            }
        } else if (!Inventory.isFull() && cowArea.contains(Players.getLocal())) {
            GameObject hide = GameObjects.getNearest("Cowhide");
            hide.interact("Take");
            Time.sleep(0, 1000);
        }

        return 162;
    }

    static int getCurrentFloor(){
        String location = Players.getLocal().getLocation().toString();
        String floor = location.substring(location.length()-2,location.length()-1);
        return Integer.valueOf(floor);
    }


}

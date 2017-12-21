package core;

import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.Random;
import org.tbot.methods.ge.GrandExchange;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.*;
import states.State;


import java.awt.*;


/**
 * Created by David on 1/17/2017.
 */

@Manifest (name = "Buy Feather Packs", authors = "Me", description = "Feather", category = ScriptCategory.MONEY_MAKING)
public class MainHandler extends AbstractScript implements PaintListener {
    String profit;
    int startCash;
    long startTime;
    double secondsElapsed;
    Area shopArea = new Area(3010, 3219, 3019, 3231);
    Area bankArea = new Area(3042, 3237, 3047, 3234);
    Area GEArea = new Area(3162, 3483, 3168, 3491);


    public boolean onStart() {
        log("Started Feather");
        startCash = Inventory.getFirst("Coins").getStackSize();
        startTime = System.currentTimeMillis();
        Time.sleep(300, 500);
        return true;
    }

    @Override
    public int loop() {
        long timeDif = (System.currentTimeMillis() - startTime) / 100;
        secondsElapsed = (double) timeDif / 10;

        int featherWorth = Inventory.getFirst("Feather").getStackSize() * 3;


        long totalProf = (featherWorth + Inventory.getFirst("Coins").getStackSize()) - startCash;
        log("Seconds Elapsed: " + secondsElapsed + " -- Profit: " + totalProf);

        int ratio = (int) ((3600 * totalProf) / secondsElapsed);
        profit = "GP/HR: " + (Integer.toString(ratio));



        int runB = Random.nextInt(57, 76);
        int runS = Random.nextInt(23, 30);
        if (Walking.getRunEnergy() >= runB && !Walking.isRunEnabled()) {
            Walking.setRun(true);
        }
        if (Walking.getRunEnergy() <= runS && Walking.isRunEnabled()) {
            Walking.setRun(false);
        }
        State state = getState();
        LogHandler.log("State: " + state);

        switch(state) {
            case WALKTOSHOP:
                Tile shopTile = new Tile(3014, 3224); //TODO SHOP TILE
                Path pathToShop = Walking.findPath(shopTile);
                if (pathToShop != null) {
                    pathToShop.traverse();
                    Time.sleepUntil(new Condition() {
                        @Override
                        public boolean check() {
                            return shopTile.distance() < 4;
                        }
                    }, Random.nextInt(700, 1400));
                }
                break;
            case BUYFROMSHOP:
                buyPacks();
                break;
            case WALKTOBANK:
                Tile boxTile = new Tile(3045, 3235); //TODO GE TILE
                Path pathToBox = Walking.findPath(boxTile);
                if (pathToBox != null) {
                    pathToBox.traverse();
                    Time.sleepUntil(new Condition() {
                        @Override
                        public boolean check() {
                            return boxTile.distance() < 4;
                        }
                    }, Random.nextInt(700, 1400));
                }
                break;
            case DEPOSIT:
                deposit();
                Time.sleep(1000, 1500);
                GrandExchange.collectAll();
                break;
            case OPENPACKS:
                openPacks();
                break;
            case WAIT:

        }
        return 136;
    }

    public State getState() {
        if (Inventory.getFirst("Coins").getStackSize() >= 500 && (!shopArea.contains(Players.getLocal())) && Inventory.getInSlot(3) == null) {
            return State.WALKTOSHOP;
        } else if (Inventory.getFirst("Coins").getStackSize() >= 500 && (shopArea.contains(Players.getLocal())) && !Inventory.isFull()) {
            return State.BUYFROMSHOP;
        } else if (Inventory.getInSlot(27).getID() == 11881) {
            return State.OPENPACKS;
        } else if (Inventory.getInSlot(2).getID() == 11881 && Inventory.getInSlot(3).getName() != null) {
            return State.WALKTOBANK;
        } else if (bankArea.contains(Players.getLocal())) {
            return State.DEPOSIT;
        } else if (Inventory.getFirst("Coins").getStackSize() < 500 && (!GEArea.contains(Players.getLocal()))) {
            return State.WALKTOGE;
//        } else if ((GEArea.contains(Players.getLocal())) && sumNum > 6 && GrandExchange.hasFreeSlots()) {
//            return State.SELLATGE;
        } else {
            return State.WAIT;
        }

    }

    public void deposit() {
        GameObject booth = GameObjects.getNearest("Bank booth");
    }

    public void buyPacks() {
        if(Inventory.getInSlot(27) != null && Inventory.getInSlot(27).getID() == 11881) {
            openPacks();

        } else if (Inventory.getInSlot(27) == null) {
            if (!Shop.isOpen()) {
                NPC gerrant = Npcs.getNearest("Gerrant");
                gerrant.interact("Trade");
                Time.sleep(706, 1500);
            } else {
                if (Shop.getItem("Feather pack").getStackSize() > 80) {
                    boolean buy5 = Random.nextInt(0, 100) < 22;
                    Time.sleep(323, 589);
                    Mouse.move(Shop.getItem("Feather pack").getRandomPoint());
                    Time.sleep(123, 231);
                    Mouse.setSpeed(Random.nextInt(9, 15));
                    if (buy5) {
                        Shop.getItem("Feather pack").interact("Buy 5");
                    } else {
                        Shop.getItem("Feather pack").interact("Buy 1");
                    }
                    Mouse.setSpeed(25);
                    Time.sleep(123, 231);
                } else {
                    openPacks();
                    Time.sleep(243, 523);
                    Game.openWorldSelect();
                    Time.sleep(1200, 1400);
                    Game.instaHopNextF2P();
                    Time.sleep(5643, 7823);
                }

            }
        }
    }

    public void openPacks() {
        Shop.close();

        for (Item i: Inventory.getItems()) {
            if (i.getID() == 11881) {
                if (Random.nextInt(0, 100) < 93) {
                    i.interact("Open");
                    Time.sleep(100, 400);
                }
            }
        }
    }

    @Override
    public void onRepaint(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString(profit, 10, 55);
        g.drawString(Double.toString(secondsElapsed), 10, 70);
    }



}

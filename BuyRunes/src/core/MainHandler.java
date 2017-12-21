package core;

import org.tbot.bot.TBot;
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


import java.awt.*;
import java.util.Arrays;
import java.util.List;


/**
 * Created by David on 1/17/2017.
 */

@Manifest (name = "BuyRunePacks", authors = "Me", description = "Add abort", category = ScriptCategory.MONEY_MAKING)
public class MainHandler extends AbstractScript implements PaintListener {
    String profit;
    int startCash;
    long startTime;
    double secondsElapsed;
    long timeDif;
    int runeWorth;
    long totalProf;
    int ratio;


    Area shopArea = new Area(3249, 3399, 3256, 3405); //TODO FIND AREA
    Area GEArea = new Area(3162, 3483, 3168, 3491); //TODO FIND GE
    String[] packs = new String[4];
    List packList;
    List runeList;

    String[] runes = new String[5];
    public boolean onStart() {
        packs[0] = "Fire rune pack";
        packs[1] = "Air rune pack";
        packs[2] = "Earth rune pack";
        packs[3] = "Mind rune pack";
        packList = Arrays.asList(packs);

        runes[0] = "Fire rune";
        runes[1] = "Air rune";
        runes[2] = "Earth rune";
        runes[3] = "Mind rune";
        runes[4] = "Death rune";
        runeList = Arrays.asList(runes);

        startCash = Inventory.getFirst("Coins").getStackSize();
        startTime = System.currentTimeMillis();

        return true;
    }

    @Override
    public int loop() {
        AntiBan.checkTimers();
        timeDif = (System.currentTimeMillis() - startTime) / 100;
        secondsElapsed = (double) timeDif / 10;

        runeWorth = (Inventory.getFirst("Air rune").getStackSize() * 5)
                + (Inventory.getFirst("Fire rune").getStackSize() * 5)
                + (Inventory.getFirst("Earth rune").getStackSize() * 5)
                + (Inventory.getFirst("Death rune").getStackSize() * 322)
                + (Inventory.getFirst("Mind rune").getStackSize() * 4);


        totalProf = (runeWorth + Inventory.getFirst("Coins").getStackSize()) - startCash;
//        log("Seconds Elapsed: " + secondsElapsed + " -- Profit: " + totalProf);

        ratio = (int) ((3600 * totalProf) / secondsElapsed);
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
                Tile shopTile = new Tile(3253, 3401); //TODO SHOP TILE
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
                buyRunes();
                break;
            case WALKTOGE:
                Tile GETile = new Tile(3165, 3487); //TODO GE TILE
                Path pathToGE = Walking.findPath(GETile);
                if (pathToGE != null) {
                    pathToGE.traverse();
                    Time.sleepUntil(new Condition() {
                        @Override
                        public boolean check() {
                            return GETile.distance() < 4;
                        }
                    }, Random.nextInt(700, 1400));
                }
                break;
            case SELLATGE:
                sellRunes();
                Time.sleep(1000, 1500);
                GrandExchange.collectAll();
                break;
            case OPENPACKS:
                openPacks();
                break;
            case WAIT:
                TBot.getBot().getScriptHandler().stopScript();

        }
        return 136;
    }

    public State getState() {
        int sumNum = Inventory.getFirst("Mind rune").getStackSize() + Inventory.getFirst("Death rune").getStackSize()
                + Inventory.getFirst("Fire rune").getStackSize() + Inventory.getFirst("Air rune").getStackSize()
                + Inventory.getFirst("Earth rune").getStackSize();

        if (Inventory.getFirst("Coins").getStackSize() >= 500 && (!shopArea.contains(Players.getLocal()))) {
            return State.WALKTOSHOP;
        } else if (Inventory.getFirst("Coins").getStackSize() >= 500 && (shopArea.contains(Players.getLocal())) && !Inventory.isFull()) {
            return State.BUYFROMSHOP;
        } else if (Inventory.getUniqueItems().size() > 6) {
            return State.OPENPACKS;
        } else if (Inventory.getFirst("Coins").getStackSize() < 500 && (!GEArea.contains(Players.getLocal()))) {
            return State.WALKTOGE;
        } else if ((GEArea.contains(Players.getLocal())) && sumNum > 6 && GrandExchange.hasFreeSlots()) {
            return State.SELLATGE;
        } else {
            return State.WAIT;
        }

    }

    public void buyRunes() {
        if(Inventory.getInSlot(27) != null) {
            openPacks();
        } else if (Inventory.getInSlot(27) == null) {
            if (Shop.isOpen()) {
                if (Shop.getItem("Death rune").getStackSize() > 0) {
                    Time.sleep(323, 589);
                    Mouse.move(Shop.getItem("Death rune").getRandomPoint());
                    Time.sleep(223, 456);
//                    Mouse.setSpeed(Random.nextInt(9, 15));
                    Shop.getItem("Death rune").interact("Buy 10");
//                    Mouse.setSpeed(25);
                    Time.sleep(343, 629);

                } else if (Shop.getItem("Air rune pack").getStackSize() > 70) {
                    Time.sleep(323, 589);
                    Mouse.move(Shop.getItem("Air rune pack").getRandomPoint());
                    Time.sleep(223, 456);
//                    Mouse.setSpeed(Random.nextInt(9, 15));
                    Shop.getItem("Air rune pack").interact("Buy 10");
//                    Mouse.setSpeed(25);
                    Time.sleep(343, 629);
                } else if (Shop.getItem("Fire rune pack").getStackSize() > 70) {
                    Time.sleep(323, 589);
                    Mouse.move(Shop.getItem("Fire rune pack").getRandomPoint());
                    Time.sleep(323, 589);
//                    Mouse.setSpeed(Random.nextInt(9, 15));
                    Shop.getItem("Fire rune pack").interact("Buy 10");
//                    Mouse.setSpeed(25);
                    Time.sleep(343, 629);
                } else if (Shop.getItem("Earth rune pack").getStackSize() > 70) {
                    Time.sleep(323, 589);
                    Mouse.move(Shop.getItem("Earth rune pack").getRandomPoint());
                    Time.sleep(223, 456);
//                    Mouse.setSpeed(Random.nextInt(9, 15));
                    Shop.getItem("Earth rune pack").interact("Buy 10");
//                    Mouse.setSpeed(25);
                    Time.sleep(343, 629);
                } else if (Shop.getItem("Air rune pack").getStackSize() > 60) {
                    Time.sleep(323, 589);
                    Mouse.move(Shop.getItem("Air rune pack").getRandomPoint());
                    Time.sleep(323, 589);
//                    Mouse.setSpeed(Random.nextInt(9, 15));
                    Shop.getItem("Air rune pack").interact("Buy 10");
//                    Mouse.setSpeed(25);
                    Time.sleep(343, 629);
                } else if (Shop.getItem("Fire rune pack").getStackSize() > 60) {
                    Time.sleep(323, 589);
                    Mouse.move(Shop.getItem("Fire rune pack").getRandomPoint());
                    Time.sleep(223, 456);
//                    Mouse.setSpeed(Random.nextInt(9, 15));
                    Shop.getItem("Fire rune pack").interact("Buy 10");
//                    Mouse.setSpeed(25);
                    Time.sleep(343, 629);
                } else if (Shop.getItem("Earth rune pack").getStackSize() > 60) {
                    Time.sleep(323, 589);
                    Mouse.move(Shop.getItem("Earth rune pack").getRandomPoint());
                    Time.sleep(323, 589);
//                    Mouse.setSpeed(Random.nextInt(9, 15));
                    Shop.getItem("Earth rune pack").interact("Buy 10");
//                    Mouse.setSpeed(25);
                    Time.sleep(343, 629);
                } else if (Shop.getItem("Air rune pack").getStackSize() > 50) {
                    Time.sleep(323, 589);
                    Mouse.move(Shop.getItem("Air rune pack").getRandomPoint());
                    Time.sleep(323, 589);
//                    Mouse.setSpeed(Random.nextInt(9, 15));
                    Shop.getItem("Air rune pack").interact("Buy 10");
//                    Mouse.setSpeed(25);
                    Time.sleep(343, 629);
                } else if (Shop.getItem("Fire rune pack").getStackSize() > 50) {
                    Time.sleep(323, 589);
                    Mouse.move(Shop.getItem("Fire rune pack").getRandomPoint());
                    Time.sleep(223, 456);
//                    Mouse.setSpeed(Random.nextInt(9, 15));
                    Shop.getItem("Fire rune pack").interact("Buy 10");
//                    Mouse.setSpeed(25);
                    Time.sleep(343, 629);
                } else if (Shop.getItem("Earth rune pack").getStackSize() > 50) {
                    Time.sleep(323, 589);
                    Mouse.move(Shop.getItem("Earth rune pack").getRandomPoint());
                    Time.sleep(223, 456);
//                    Mouse.setSpeed(Random.nextInt(9, 15));
                    Shop.getItem("Earth rune pack").interact("Buy 10");
//                    Mouse.setSpeed(25);
                    Time.sleep(343, 629);
                } else if (Shop.getItem("Mind rune pack").getStackSize() > 25) {
                    Time.sleep(323, 589);
                    Mouse.move(Shop.getItem("Mind rune pack").getRandomPoint());
                    Time.sleep(323, 589);
//                    Mouse.setSpeed(Random.nextInt(9, 15));
                    Shop.getItem("Mind rune pack").interact("Buy 10");
//                    Mouse.setSpeed(25);
                    Time.sleep(343, 629);
                } else {
                    openPacks();
                    Time.sleep(243, 523);
                    Game.openWorldSelect();
                    Time.sleep(756, 999);
                    Game.instaHopNextF2P();
                    Time.sleep(5643, 7823);
                }
            } else {
                NPC aubury = Npcs.getNearest("Aubury");
                aubury.interact("Trade");
                Time.sleep(706, 1500);

            }
        }
    }

    public void openPacks() {
        Shop.close();

        for (Item i: Inventory.getItems()) {
            if (packList.contains(i.getName())) {
                if (Random.nextInt(0, 100) < 93) {
                    i.interact("Open");
                    Time.sleep(50, 500);
                }
            }
        }
    }


    public void sellRunes() {
        if (Random.nextInt(0, 100) > 62) {
            NPC GEBanker = Npcs.getNearest("Grand Exchange Clerk");
            GEBanker.interact("Exchange");
            Time.sleep(1200, 1600);
        } else {
            GameObject booth = GameObjects.getNearest("Grand Exchange booth");
            booth.interact("Exchange");
            Time.sleep(1200, 1600);
        }
        if (!GrandExchange.isMainScreenOpen()) {
            GrandExchange.openMainScreen();
            Time.sleep(1200, 1600);
        }

        GrandExchange.collectAll();
        Time.sleep(800, 1100);

        Item air = Inventory.getFirst("Air rune");
        Item fire = Inventory.getFirst("Fire rune");
        Item earth = Inventory.getFirst("Earth rune");
        Item mind = Inventory.getFirst("Mind rune");
        Item death = Inventory.getFirst("Death rune");

        if (death.getStackSize() > 2) {
            sellDeath(death);
        } else if (fire.getStackSize() > 100) {
            sellItem(fire);
        } else if (air.getStackSize() > 100) {
            sellItem(air);
        } else if (earth.getStackSize() > 100) {
            sellItem(earth);
        } else if (mind.getStackSize() > 100) {
            sellMind(mind);
        }

    }

    public void sellItem(Item i) {
        GrandExchange.placeSellOffer(i);
        Time.sleep(500, 800);
        i.click();
        Time.sleep(500, 800);
        Mouse.move(Random.nextInt(52, 55), Random.nextInt(182, 188)); //TODO minus 1
        Time.sleep(500, 800);
        Mouse.click(true);
        Time.sleep(500,800);
        Mouse.move(Random.nextInt(377, 405), Random.nextInt(205, 219)); //TODO price
        Time.sleep(500, 800);
        Mouse.click(true);
        Time.sleep(1000, 2000);
        org.tbot.methods.input.keyboard.Keyboard.sendText("5", true, 100, 300);
        Time.sleep(500, 800);
        Mouse.move(Random.nextInt(193, 328), Random.nextInt(272, 300)); //TODO COMFIRM
        Time.sleep(500, 800);
        Mouse.click(true);
        Time.sleep(600,700);
    }

    public void sellMind(Item i) {
        GrandExchange.placeSellOffer(i);
        Time.sleep(500, 800);
        i.click();
        Time.sleep(500, 800);
        Mouse.move(Random.nextInt(52, 55), Random.nextInt(182, 188)); //TODO minus 1
        Time.sleep(500, 800);
        Mouse.click(true);
        Time.sleep(500,800);
        Mouse.move(Random.nextInt(377, 405), Random.nextInt(205, 219)); //TODO price
        Time.sleep(500, 800);
        Mouse.click(true);
        Time.sleep(1000, 2000);
        org.tbot.methods.input.keyboard.Keyboard.sendText("4", true, 100, 300);
        Time.sleep(500, 800);
        Mouse.move(Random.nextInt(193, 328), Random.nextInt(272, 300)); //TODO COMFIRM
        Time.sleep(500, 800);
        Mouse.click(true);
        Time.sleep(600,700);
    }

    public void sellDeath(Item i) {
        GrandExchange.placeSellOffer(i);
        Time.sleep(500, 800);
        i.click();
        Time.sleep(500, 800);
        Mouse.move(Random.nextInt(52, 55), Random.nextInt(182, 188)); //TODO minus 1
        Time.sleep(500, 800);
        Mouse.click(true);
        Time.sleep(500,800);
        Mouse.move(Random.nextInt(193, 328), Random.nextInt(272, 300)); //TODO COMFIRM
        Time.sleep(500, 800);
        Mouse.click(true);
        Time.sleep(600,700);
    }

    @Override
    public void onRepaint(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString(profit, 10, 55);
        g.drawString(Double.toString(secondsElapsed), 10, 70);
    }

    public enum State {
        WALKTOSHOP,
        WALKTOGE,
        SELLATGE,
        BUYFROMSHOP,
        OPENPACKS,
        WAIT,
        CHECKABORT
    }



}

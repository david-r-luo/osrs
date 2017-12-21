package core;

/**
 * Created by David on 1/21/2017.
 */

import org.tbot.bot.TBot;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.ge.GrandExchange;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.*;


/**
 * Created by David on 1/21/2017.
 */
@Manifest(name = "SellHidesGE", authors = "me")
public class SellHide extends AbstractScript {
    Tile GETile = new Tile(3165, 3487); //TODO GE TILE
    Area GEArea = new Area(3162, 3483, 3168, 3491); //TODO FIND GE

    public int start() {
        log("Sell Hide");
        return 399;

    }

    @Override
    public int loop() {
        State state = getState();
        log(state);
        if (state == State.WALKTOGE) {
            Path pathToGE = Walking.findPath(GETile);
            if (pathToGE != null) {
                pathToGE.traverse();
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return GETile.distance() < 4;
                    }
                }, Random.nextInt(800, 1200));
            }

        } else if (state == State.WITHDRAW) {
            if (Random.nextInt(0, 100) > 52) {
                NPC banker = Npcs.getNearest("Banker");
                banker.interact("Bank");
                Time.sleep(1200, 1600);
            } else {
                GameObject booth = GameObjects.getNearest("Grand Exchange booth");
                booth.interact("Bank");
                Time.sleep(1200, 1600);

            }
            Bank.open();
            Time.sleep(1000, 1400);
            Bank.setNoted(true);
            Time.sleep(500, 700);
            Bank.withdrawAll("Cowhide");
            Time.sleep(1200, 1600);
            Bank.close();
            Time.sleep(1000, 1400);

        } else if (state == State.SELLATGE) {

            if (Random.nextInt(0, 100) > 62) {
                NPC GEBanker = Npcs.getNearest("Grand Exchange Clerk");
                GEBanker.interact("Exchange");
                Time.sleep(1200, 1600);
            } else {
                GameObject booth = GameObjects.getNearest("Grand Exchange booth");
                booth.interact("Exchange");
                Time.sleep(1200, 1600);
            }

            Item hide = Inventory.getFirst("Cowhide");
            GrandExchange.placeSellOffer(hide);
            Time.sleep(500, 800);
            hide.click();
            Time.sleep(500, 800);
            Mouse.move(Random.nextInt(193, 328), Random.nextInt(272, 300));
            Time.sleep(500, 800);
            Mouse.click(true);
            Time.sleep(5000,7000);
            GrandExchange.collectAll();
            TBot.getBot().getScriptHandler().stopScript();
        }
        return 147;
    }

    public State getState() {
        if (!GEArea.contains(Players.getLocal())) {
            return State.WALKTOGE;
        } else if (GEArea.contains(Players.getLocal()) && !Inventory.contains("Cowhide")) {
            return State.WITHDRAW;
        } else if (GEArea.contains(Players.getLocal()) && Inventory.contains("Cowhide")) {
            return State.SELLATGE;
        }
        return null;
    }

    public enum State {
        WALKTOGE,
        WITHDRAW,
        SELLATGE
    }




}


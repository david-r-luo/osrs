package core;

import org.tbot.bot.TBot;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.methods.*;
import org.tbot.methods.ge.GrandExchange;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.Item;
import org.tbot.wrappers.NPC;
import org.tbot.wrappers.Tile;

/**
 * Created by David on 1/21/2017.
 */
@Manifest(name = "SellHidesGE", authors = "me")
public class MainHandler extends AbstractScript {


    @Override
    public boolean onStart() {
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

        if (Random.nextInt(0, 100) > 52) {
            NPC banker = Npcs.getNearest("Banker");
            banker.interact("Bank");
            Time.sleep(1200, 1600);
        } else {
            GameObject booth = GameObjects.getNearest("Bank booth");
            booth.interact("Bank");
            Time.sleep(1200, 1600);
        }

        Bank.withdrawAll("Cowhide");
        Time.sleep(1200, 1600);
        Bank.close();
        Time.sleep(1000, 1400);

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
        Time.sleep(600,700);

        TBot.getBot().getScriptHandler().stopScript();

        return true;
    }

    @Override
    public int loop() {
        return 147;
    }




}
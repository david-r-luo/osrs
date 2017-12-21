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
import org.tbot.wrappers.Item;
import org.tbot.wrappers.NPC;
import org.tbot.wrappers.Tile;

/**
 * Created by David on 1/21/2017.
 */
@Manifest(name = "Buy Jugs")
public class BuyJugs extends AbstractScript{
    Area storeArea = new Area(3213, 3409, 3221, 3420);
    Tile storeTile = new Tile(3217, 3415);

    public boolean onStart() {
        log("Start Jugs");

        return true;
    }
//12 secs

    @Override
    public int loop() {
        State state = getState();
        log(state);
        if (state == State.WALKTOSTORE) {
            Path pathToStore = Walking.findPath(storeTile);
            if (pathToStore != null) {
                pathToStore.traverse();
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return storeTile.distance() < 4;
                    }
                }, Random.nextInt(800, 1200));
            }
        } else if (state == State.BUYJUGS) {
            if (Shop.isOpen()) {
                if (Shop.getItem("Empty jug pack").getStackSize() > 4) {
                    Mouse.move(Shop.getItem("Empty jug pack").getRandomPoint());
                    Time.sleep(223, 456);
                    Shop.getItem("Empty jug pack").interact("Buy 5");
                    Time.sleep(343, 629);
                    Game.openWorldSelect();
                    Time.sleep(756, 999);
                    Game.instaHopRandomF2P();
                    Time.sleep(5643, 7823);
                }
            } else {
                NPC shop = Npcs.getNearest("Shop keeper");
                shop.interact("Trade");
            }
        } else if (state == State.OPENJUGS) {
            if (Inventory.contains("Empty jug pack")) {
                TBot.getBot().getScriptHandler().stopScript();
            } else {
                Shop.close();

                for (Item i: Inventory.getItems()) {
                    if (i.getName() == "Empty jug pack") {
                        if (Random.nextInt(0, 100) < 93) {
                            i.interact("Open");
                            Time.sleep(50, 500);
                        }
                    }
                }
            }

        } else if (state == State.END) {
            TBot.getBot().getScriptHandler().stopScript();
        }
        return 145;
    }

    public State getState() {
        if (Inventory.getFirst("Coins").getStackSize() > 200 && !storeArea.contains(Players.getLocal())) {
            return State.WALKTOSTORE;
        } else if (Inventory.getFirst("Coins").getStackSize() > 200 && storeArea.contains(Players.getLocal()) && !Inventory.isFull()
                && (Inventory.getFirst("Jug").getStackSize() + (Inventory.getFirst("Empty jug pack").getStackSize()*100) < 4300)) {
            return State.BUYJUGS;
        } else if (Inventory.contains("Empty jug pack") || Inventory.getFirst("Coins").getStackSize() <= 200) {
            return State.OPENJUGS;
        }

        return State.END;
    }

    public enum State {
        WALKTOSTORE,
        BUYJUGS,
        OPENJUGS,
        END
    }
}

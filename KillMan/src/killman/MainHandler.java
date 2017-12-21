package killman;

import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.handlers.LogHandler;

import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.*;

/**
 * Created by David on 1/14/2017.
 */

@Manifest(authors = "joeysaladarse", description = "Kill Lumbridge Men", version = 0.01, name = "KillManLumbridge", category = ScriptCategory.COMBAT)

public class MainHandler extends AbstractScript {

    public static final Area LUMBY = new Area(new Tile[]{new Tile(3213, 3225, 0), new Tile(3224, 3212, 0), new Tile(3223, 3224, 0)});
    public int timeSpent;

    public boolean onStart() {
        LogHandler.log("Kill Kill Kill Kill Kill");
        return true;
    }

    @Override
    public int loop() {
        final NPC man = Npcs.getNearest("Man");


        if (Players.getLocal().isDead()) {
            Time.sleep(2394, 3953);
            for(Item i : Inventory.getItems()) {
                if ((i.getName() == "Bronze sword") || (i.getName() == "Wooden Shield")) {
                    i.click();
                }
            }
        }

        if(!LUMBY.contains(man.getLocation().getX(), man.getLocation().getY())) {
            Path pathToLum = Walking.findPath(LUMBY. getCentralTile());
            if (pathToLum != null) {
                pathToLum.traverse();
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return man != null;
                    }
                }, Random.nextInt(700, 1400));
            }
        } else if (man != null && man.isOnScreen()) {
            if (Players.getLocal().getAnimation() == -1) {
                if (man.interact("Attack")) {
                    Time.sleepUntil(new Condition() {
                        @Override
                        public boolean check() {
                            return !Players.getLocal().isHealthBarVisible();
                        }
                    }, Random.nextInt(1200, 2100));

                }

            }
        } else if (man != null && !man.isOnScreen()){
            if (man.distance() > 8) {
                Path pathToMan = Walking.findPath(man);
                if (pathToMan != null) {
                    pathToMan.traverse();
                    Time.sleepUntil(new Condition() {
                        @Override
                        public boolean check() {
                            return man.distance() < 5 || man.isOnScreen();
                        }
                    }, Random.nextInt(1300, 2400));
                }
            } else if (man.distance() <= 8) {
                Camera.turnTo(man);
                Time.sleep(300, 480);
            }

        } else if (man == null) {
            Path pathToLum = Walking.findPath(LUMBY. getCentralTile());
            if (pathToLum != null) {
                pathToLum.traverse();
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return man != null;
                    }
                }, Random.nextInt(800, 1600));
            }

        }

        return 120;
    }
}

package core;

import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.wrappers.Area;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.Tile;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by David on 1/15/2017.
 */

@Manifest(name = "Clay Mining", authors = "joeysaladars", category = ScriptCategory.MINING, description = "Mining clay in Varrock")
public class MainHandler extends AbstractScript implements PaintListener{
    private String level;
    private Area bankArea = new Area(3180, 3433, 3189, 3438);
    private Tile clayTile = new Tile(3180, 3371);
    private State state;
    private String pickaxe;
    private String[] pickaxes = new String[7];

    GameObject clay = GameObjects.getNearest(7454, 7487);

    public boolean onStart() {
        LogHandler.log("Started Clay Mining");
        pickaxes[6] = "Bronze pickaxe";
        pickaxes[5] = "Iron pickaxe";
        pickaxes[4] = "Steel pickaxe";
        pickaxes[3] = "Black pickaxe";
        pickaxes[2] = "Mithril pickaxe";
        pickaxes[1] = "Adamant pickaxe";
        pickaxes[0] = "Rune pickaxe";

        for (String item: Inventory.getUniqueItemNames()){
            for (String pick: pickaxes) {
                if (pick == item) {
                    pickaxe = item;
                    break;
                }
            }
        }

        return true;
    }

    @Override
    public int loop() {
        int runB = Random.nextInt(57, 76);
        int runS = Random.nextInt(23, 30);
        if (Walking.getRunEnergy() >= runB && !Walking.isRunEnabled()) {
            Walking.setRun(true);
        }
        if (Walking.getRunEnergy() <= runS && Walking.isRunEnabled()) {
            Walking.setRun(false);
        }

        state = getState();
        if (state == State.MINE) {
            Mine(clay);
        } else if (state == State.BANK) {
            GameObject booth = GameObjects.getNearest("Bank booth");
            BankClass(booth);
        } else if (state == State.WALK_TO_BANK) {
            WalkToBank();
        } else if (state == State.WALK_TO_MINE) {
            WalkToMine();
        }

        return 130;
    }


    public State getState() {
        if(Inventory.isFull() && !bankArea.contains(Players.getLocal())) {
            return State.WALK_TO_BANK;
        } else if(!Inventory.isFull() && (Players.getLocal().getLocation().equals(clayTile))) {
            return State.MINE;
        } else if(!Inventory.isFull() && !(Players.getLocal().getLocation().equals(clayTile))) {
            return State.WALK_TO_MINE;
        } else if(Inventory.isFull() && bankArea.contains(Players.getLocal())) {
            return State.BANK;
        } else {
            return null;
        }
    }

    public void Mine(GameObject clay) {
        if (Inventory.isFull()) {
            state = State.WALK_TO_BANK;
        } else if (clay != null && clay.isOnScreen() && clay.getID() != 3) {
            log(clay.getID());
            clay.interact("Mine");
            Time.sleep(190, 423);
        } else {
            Camera.rotateRandomly();
            Camera.turnTo(clay);
        }
    }

    public void BankClass (GameObject booth){
        if (booth != null && booth.isOnScreen()) {
            if (Bank.isOpen()) {
                Bank.depositAllExcept(pickaxe);
                Time.sleep(139, 523);
                Bank.close();
                Time.sleep(167, 823);
            } else {
                Bank.open();
                Time.sleep(300,1000);
            }
        } else {
            int x = Random.nextInt(1, 100);
            if (x < 42) {
                Camera.rotateRandomly();
            }
            Camera.turnTo(booth);
        }
    }

    public void WalkToBank () {
        Tile bankTile = new Tile(3185, 3437);
        Path pathToBank = Walking.findPath(bankTile);
        pathToBank.traverse();
        Time.sleep(173,446);
    }

    public void WalkToMine () {
        Tile mineTile = new Tile(3182, 3371);
        Path pathToMine = Walking.findPath(mineTile);
        pathToMine.traverse();
        Time.sleep(213,363);
    }





    @Override
    public void onRepaint(Graphics g) {
        g.setColor(Color.BLUE);
        level = "Current Level: " + Skills.getCurrentLevel(Skills.Skill.MINING);
        g.drawString(level, 7, 55);
    }

    public enum State {
        MINE,
        BANK,
        WALK_TO_BANK,
        WALK_TO_MINE,

    }
}

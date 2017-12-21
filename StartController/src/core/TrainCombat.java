package core;

import org.tbot.bot.TBot;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Equipment;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.Area;
import org.tbot.wrappers.NPC;
import org.tbot.wrappers.Tile;

/**
 * Created by David on 1/21/2017.
 */
@Manifest(name = "TrainCombat", authors = "me")
public class TrainCombat extends AbstractScript {
    Area cowArea = new Area(new Tile[]{new Tile(3252, 3254), new Tile(3252, 3271), new Tile(3240, 3283), new Tile(3240, 3299), new Tile(3266, 3299)});
    Area chickenArea = new Area(new Tile[]{new Tile(3237, 3286), new Tile(3237, 3301), new Tile(3226, 3301), new Tile(3224, 3295), new Tile(3230, 3294), new Tile(3231, 3287)});
    Tile chickenTile = new Tile(3232, 3295);
    Tile chickenTile2 = new Tile(3228, 3300);
    NPC chicken;
    public boolean onStart() {

        return true;
    }

    @Override
    public int loop() {
        State state = getState();
        log(state);
        if (state == State.GETEQUIP) {
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
            Time.sleep(700, 1000);
            Bank.depositAll();
            Time.sleep(700, 1000);
            Bank.depositAllEquipment();
            Time.sleep(700, 1000);
            Bank.withdraw("Bronze sword", 1);
            Time.sleep(500, 800);
            Bank.withdraw("Wooden shield", 1);
            Time.sleep(500, 800);
            Bank.close();


        } else if (state == State.WALKTOCHICKEN) {

        } else if (state == State.FIGHT) {
            if (!chickenArea.contains(Players.getLocal())) {
                Path chickenPath = Walking.findPath(chickenTile);
                if (chickenPath != null) {
                    chickenPath.traverse();
                    Time.sleepUntil(new Condition() {
                        @Override
                        public boolean check() {
                            return chickenTile.distance() < 3;
                        }
                    }, Random.nextInt(400, 700));
                }
            } else if (Skills.getCurrentLevel(Skills.Skill.ATTACK) >= 16) { //TODO add &&currentStyle is ATT
                Widgets.openTab(Widgets.TAB_COMBAT);


                //switch to str


            } else if (Skills.getCurrentLevel(Skills.Skill.STRENGTH) >= 12) {
                //switch to def
            } else if (Skills.getCurrentLevel(Skills.Skill.DEFENCE) >= 10) {
                TBot.getBot().getScriptHandler().stopScript();
            } else {
                //fight chickens
                chicken = Npcs.getNearest("Chicken");
                if (!chicken.isHealthBarVisible() && !Players.getLocal().isHealthBarVisible()) {
                    chicken.interact("Attack");
                } else {
                    if (chickenTile.distance() > 1) {
                        Walking.findPath(chickenTile).traverse();
                        Time.sleepUntil(new Condition() {
                            @Override
                            public boolean check() {
                                return chickenTile.distance() < 2;
                            }
                        }, Random.nextInt(700, 1000));
                    } else {
                        Walking.findPath(chickenTile2).traverse();
                    }
                }
            }
        }
        return Random.nextInt(110, 200);
    }

    public State getState() {
        if (Equipment.getItemInSlot(Equipment.SLOTS_WEAPON).getName().equals("Bronze sword")
                && Equipment.getItemInSlot(Equipment.SLOTS_SHIELD).getName().equals("Wooden shield")) {
            return State.GETEQUIP;
        } else if (Equipment.getItemInSlot(Equipment.SLOTS_WEAPON).getName().equals("Bronze sword")
                && Equipment.getItemInSlot(Equipment.SLOTS_SHIELD).getName().equals("Wooden shield")
                && !chickenArea.contains(Players.getLocal())) {
            return State.WALKTOCHICKEN;

        } else if (Equipment.getItemInSlot(Equipment.SLOTS_WEAPON).getName().equals("Bronze sword")
                && Equipment.getItemInSlot(Equipment.SLOTS_SHIELD).getName().equals("Wooden shield")
                && chickenArea.contains(Players.getLocal())) {
            return State.FIGHT;

        }

        return State.END;

    }

    public enum State {
        GETEQUIP,
        WALKTOCHICKEN,
        FIGHT,
        END

    }

}

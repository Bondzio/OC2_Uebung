import jnibwapi.BWAPIEventListener;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

import java.util.HashSet;

public class VultureAI  implements BWAPIEventListener, Runnable {

    private final JNIBWAPI bwapi;

    private Vulture vulture;

    private HashSet<Unit> enemyUnits;

    private int frame;
    private int marineID = 0;

    public VultureAI() {
        System.out.println("This is the VultureAI! :)");

        bwapi = new JNIBWAPI(this, false);
    }

    public static void main(String[] args) {
        new VultureAI().run();
    }

    @Override
    public void matchStart() {
        enemyUnits = new HashSet<Unit>();

        frame = 0;

        bwapi.enablePerfectInformation();
        bwapi.enableUserInput();
        bwapi.setGameSpeed(20);
    }

    @Override
    public void matchFrame() {

        vulture.step();

        if (frame % 1000 == 0) {
            System.out.println("Frame: " + frame);
        }
        frame++;
    }

    @Override
    public void unitDiscover(int unitID) {
        Unit unit = bwapi.getUnit(unitID);
        int typeID = unit.getTypeID();

        if (typeID == UnitType.UnitTypes.Terran_Vulture.getID()) {
            if (unit.getPlayerID() == bwapi.getSelf().getID()) {
                this.vulture = new Vulture(unit, bwapi, enemyUnits, marineID);
                System.out.println("found vulture");
            }
        } else if (typeID == UnitType.UnitTypes.Protoss_Zealot.getID()) {
            if (unit.getPlayerID() != bwapi.getSelf().getID()) {
                enemyUnits.add(unit);
                System.out.println("found zealot");
            }
        }
    }

    @Override
    public void unitDestroy(int unitID) {
        Unit rmUnit = null;
        for (Unit u : enemyUnits) {
            if (u.getID() == unitID) {
                rmUnit = u;
                break;
            }
        }
        enemyUnits.remove(rmUnit);
    }

    @Override
    public void connected() {
        System.out.println("Connected");
    }

    @Override
    public void matchEnd(boolean winner) {
    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void sendText(String text) {

    }

    @Override
    public void receiveText(String text) {

    }

    @Override
    public void playerLeft(int playerID) {

    }

    @Override
    public void nukeDetect(Position position) {

    }

    @Override
    public void nukeDetect() {

    }

    @Override
    public void unitEvade(int unitID) {

    }

    @Override
    public void unitShow(int unitID) {

    }

    @Override
    public void unitHide(int unitID) {

    }

    @Override
    public void unitCreate(int unitID) {
    }

    @Override
    public void unitMorph(int unitID) {

    }

    @Override
    public void unitRenegade(int unitID) {

    }

    @Override
    public void saveGame(String gameName) {

    }

    @Override
    public void unitComplete(int unitID) {

    }

    @Override
    public void playerDropped(int playerID) {

    }

    @Override
    public void run() {
        bwapi.start();
    }
}

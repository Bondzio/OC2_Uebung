import jnibwapi.BWAPIEventListener;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

import java.util.HashSet;

public class AnanasAI implements BWAPIEventListener, Runnable {

    private final JNIBWAPI bwapi;

    private final int LEARNING_MATCHES = 100;
    private final String[] LEARNING_GA = new String[]{"ga1", "ga2", "ga3", "ga4"};
    private final boolean stop_learning = false;
    
    private Vulture vulture;
    private int matches = 0;
    private int gaSelector = 0;

    private HashSet<Unit> enemyUnits;

    private int frame;
//    private int marineID = 0; // from old Version

    public AnanasAI() {
        System.out.println("This is the ANANAS_AI ! :)");

        bwapi = new JNIBWAPI(this, false);
    }

    public static void main(String[] args) {
        new AnanasAI().run();
    }

    @Override
    public void matchStart() {
        enemyUnits = new HashSet<Unit>();

        frame = 0;
        
        bwapi.enablePerfectInformation();
        bwapi.enableUserInput();
        bwapi.setGameSpeed(0);
    }

    @Override
    public void matchFrame() {

    	if (matches >= LEARNING_MATCHES){
    		matches = 0;
    		gaSelector++;
    		
    		if(gaSelector >= LEARNING_GA.length){
    			System.out.println("LEARNING FINISHED!");
    			System.exit(1);
       		}
    	}

        try {
        	vulture.step(LEARNING_GA[gaSelector],stop_learning);
                

            if (frame % 1000 == 0) {
                System.out.println("Frame: " + frame);
            }
            frame++;

        }catch (NullPointerException np){
            System.out.println("Game is restarting...");
        }
    }

    @Override
    public void unitDiscover(int unitID) {
        Unit unit = bwapi.getUnit(unitID);
        UnitType type = unit.getType();

        if (type == UnitType.UnitTypes.Terran_Vulture) {
            if (unit.getPlayer() == bwapi.getSelf()) {
                this.vulture = new Vulture(unit, bwapi, enemyUnits);
            }
        } else if (type == UnitType.UnitTypes.Protoss_Zealot) {
            if (unit.getPlayer() != bwapi.getSelf()) {
                enemyUnits.add(unit);
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
        Unit vultureUnit = vulture.getMyUnit();
        int hpVulture = vultureUnit.getHitPoints();
        int kills= vultureUnit.getKillCount();

        int cAttackMove = vulture.getCountAttackMove();
        int cKite = vulture.getCountKite();

        vulture.xcs_Manager.makeNewMatchStat(frame,hpVulture,kills,cAttackMove,cKite);
        vulture.xcs_Manager.cleanUp();
        matches++;
        System.out.println("MatchOver - Matches:" + matches);
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

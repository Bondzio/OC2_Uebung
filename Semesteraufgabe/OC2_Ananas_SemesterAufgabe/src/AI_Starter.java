import AI.AnanasAI;
import jnibwapi.BWAPIEventListener;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;

public class AI_Starter implements BWAPIEventListener, Runnable {

    private final JNIBWAPI bwapi;

    private final AnanasAI ananasAI;


    private int frame;
//    private int marineID = 0; // from old Version

    public AI_Starter() {
        bwapi = new JNIBWAPI(this, false);
        this.ananasAI = new AnanasAI(bwapi);
    }

    public static void main(String[] args) {
        new AI_Starter().run();
    }

    @Override
    public void matchStart() {

        frame = 0;
        
        //bwapi.enablePerfectInformation();
        bwapi.enableUserInput();
        bwapi.setGameSpeed(1);
    }

    @Override
    public void matchFrame() {

//    	if (matches >= LEARNING_MATCHES){
//    		matches = 0;
//    		gaSelector++;
//
//    		if(gaSelector >= LEARNING_GA.length){
//    			System.out.println("LEARNING FINISHED!");
//    			System.exit(1);
//       		}
//    	}

//        try {
        	//vulture.step(LEARNING_GA[gaSelector],stop_learning);
              this.ananasAI.doStepAll();
            //System.out.println("ID IS:" + bwapi.getSelf().getID());

            if (frame % 1000 == 0) {
                System.out.println("Frame: " + frame);
            }
            frame++;
//
//        }catch (NullPointerException np){
//            System.out.println("Game is restarting...");
//        }
    }

    @Override
    public void unitDiscover(int unitID) {
        this.ananasAI.addUnit(unitID);
    }

    @Override
    public void unitDestroy(int unitID) {
        this.ananasAI.removeUnit(unitID);
    }

    @Override
    public void connected() {
        System.out.println("Connected");
    }

    @Override
    public void matchEnd(boolean winner) {
        this.ananasAI.matchEnd(winner);
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

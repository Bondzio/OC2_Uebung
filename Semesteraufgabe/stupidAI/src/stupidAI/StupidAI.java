package stupidAI;

import jnibwapi.BWAPIEventListener;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;


public class StupidAI implements BWAPIEventListener {
	
	private final JNIBWAPI bwapi;
	
	private String color;
    private int counter = 0;

	public static void main(String[] args) {
		new StupidAI();
	}

	public StupidAI() {
		bwapi = new JNIBWAPI(this, true);
		bwapi.start();
	}

	@Override
	public void connected() {
		System.out.println("Connected");
	}
	

	@Override
	public void matchStart() {
		System.out.println("Game Started");

		bwapi.setGameSpeed(0);

		if(bwapi.getSelf().getColor() == 111)
        	color = "red";
        else 
        	color = "blue";
	}
	

	@Override
	public void matchFrame() {
	
		// attack move toward an enemy
		for (Unit unit : bwapi.getMyUnits()) {
			if (unit.isIdle()) {
				if (bwapi.getEnemyUnits().isEmpty()){
					checkTarget(unit);
					unit.attack(getHatchery(), false);
				}
				else{
					for (Unit enemy : bwapi.getEnemyUnits()) {
						unit.attack(enemy.getPosition(), false);
						break;
					}
				}
			}
		}
	}
	
	private void checkTarget(Unit unit){
		if(bwapi.getEnemyUnits().isEmpty() && unit.getDistance(getHatchery()) < 200){
			counter = (counter + 1) % 2;
		}
	}
	
	private Position getHatchery(){
		Position[] redHatcheries = new Position[] {new Position(516, 368), new Position(516, 2704)};
		Position[] blueHatcheries = new Position[] {new Position(3420,368),  new Position(3420, 2704)};
		
		if(color == "red")
			return blueHatcheries[counter];
		else
			return redHatcheries[counter];
	}
	
	
	@Override
	public void keyPressed(int keyCode) {}
	@Override
	public void matchEnd(boolean winner) {}
	@Override
	public void sendText(String text) {}
	@Override
	public void receiveText(String text) {}
	@Override
	public void nukeDetect(Position p) {}
	@Override
	public void nukeDetect() {}
	@Override
	public void playerLeft(int playerID) {}
	@Override
	public void unitCreate(int unitID) {}
	@Override
	public void unitDestroy(int unitID) {	}
	@Override
	public void unitDiscover(int unitID) {}
	@Override
	public void unitEvade(int unitID) {}
	@Override
	public void unitHide(int unitID) {}
	@Override
	public void unitMorph(int unitID) {}
	@Override
	public void unitShow(int unitID) {}
	@Override
	public void unitRenegade(int unitID) {}
	@Override
	public void saveGame(String gameName) {}
	@Override
	public void unitComplete(int unitID) {}
	@Override
	public void playerDropped(int playerID) {}
}

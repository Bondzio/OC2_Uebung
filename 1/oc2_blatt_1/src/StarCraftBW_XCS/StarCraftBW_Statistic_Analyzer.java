package StarCraftBW_XCS;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class StarCraftBW_Statistic_Analyzer extends Application{
	
	private ArrayList<StarCraftBW_MatchStat> mStats = new ArrayList<StarCraftBW_MatchStat>();

	@Override
	public void start(Stage stage) {
		setMatchStats();
		
		stage.setTitle("ANANAS VultureAI");
		// defining the axes
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Executions");
		// creating the chart
		final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(
				xAxis, yAxis);

		lineChart.setTitle("Learning Progress");
		// defining a series
		XYChart.Series frames = new XYChart.Series();
        XYChart.Series hp = new XYChart.Series();
    	XYChart.Series kills = new XYChart.Series();
        XYChart.Series moves = new XYChart.Series();
    	XYChart.Series kites = new XYChart.Series();

        frames.setName("Frames");
        hp.setName("Healthpoints");
		kills.setName("Kills");
		moves.setName("AttackMoves");
		kites.setName("Kites");
		
		for(int i = 0; i < mStats.size(); i++){
			frames.getData().add(new XYChart.Data(mStats.get(i).id, mStats.get(i).frames));
			hp.getData().add(new XYChart.Data(mStats.get(i).id, mStats.get(i).hp));
			kills.getData().add(new XYChart.Data(mStats.get(i).id, mStats.get(i).kills));
			moves.getData().add(new XYChart.Data(mStats.get(i).id, mStats.get(i).countAttackMove));
			kites.getData().add(new XYChart.Data(mStats.get(i).id, mStats.get(i).countKite));
		}
		
		Scene scene = new Scene(lineChart, 800, 600);
		lineChart.getData().addAll(frames, hp, kills, moves, kites);

		stage.setScene(scene);
		stage.show();
	}
	
	private void setMatchStats(){
		StarCraftBW_FileThread fileThread = new StarCraftBW_FileThread();
		StarCraftBW_MatchStats matchStats = fileThread.getSavedMatchStats();
		this.mStats = matchStats.getMatchStats();
		fileThread.stopMe();
	}
	
	public static void main(String[] args) {
		launch();
	}
}
package StarCraftBW_XCS;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StarCraftBW_Statistic_Analyzer extends Application{
	
	private ArrayList<StarCraftBW_MatchStat> mStats = new ArrayList<StarCraftBW_MatchStat>();
    private final String[] LEARNING_GA = new String[]{"ga1", "ga2","ga3"};
    private TabPane tabPane = new TabPane();

	@Override
	public void start(Stage stage) {

		stage.setTitle("ANANAS VultureAI");
		
		for(int ga = 0; ga < LEARNING_GA.length; ga++){
			setMatchStats(LEARNING_GA[ga]);
			
			if(!mStats.isEmpty())			
				createLineCharts(ga);
		}
		
		Scene scene = new Scene(tabPane, 1000, 800);
		stage.setScene(scene);
		stage.show();
	}
	
	private void createLineCharts(int ga){
		SplitPane splitPane = new SplitPane();
		
		Tab tab = new Tab();
		tab.setText(LEARNING_GA[ga]);
	
		SplitPane splitPane1 = new SplitPane();
		SplitPane splitPane2 = new SplitPane();
		
		BorderPane pane = new BorderPane();
		BorderPane pane2 = new BorderPane();
		
		XYChart.Series series1 = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();
		
		// defining the axes
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Executions");
		
		// creating the chart
		final LineChart<Number, Number> lineChart1 = new LineChart<Number, Number>(xAxis, yAxis);

		lineChart1.setTitle("Learning Progress");
		// defining a series
		XYChart.Series frames = new XYChart.Series();
        XYChart.Series moves = new XYChart.Series();
    	XYChart.Series kites = new XYChart.Series();

        frames.setName("Frames");
		moves.setName("AttackMoves");
		kites.setName("Kites");
		
		for(int i = 0; i < mStats.size(); i++){
			frames.getData().add(new XYChart.Data(mStats.get(i).id, mStats.get(i).frames));
			moves.getData().add(new XYChart.Data(mStats.get(i).id, mStats.get(i).countAttackMove));
			kites.getData().add(new XYChart.Data(mStats.get(i).id, mStats.get(i).countKite));
		}
		
		
		//lineChart2
		final NumberAxis xAxis2 = new NumberAxis();
		final NumberAxis yAxis2 = new NumberAxis();

		final LineChart<Number, Number> lineChart2 = new LineChart<Number, Number>(xAxis2, yAxis2);
		
		// defining a series
        XYChart.Series hp = new XYChart.Series();
    	XYChart.Series kills = new XYChart.Series();

        hp.setName("Healthpoints");
		kills.setName("Kills");

		for(int i = 0; i < mStats.size(); i++){
			hp.getData().add(new XYChart.Data(mStats.get(i).id, mStats.get(i).hp));
			kills.getData().add(new XYChart.Data(mStats.get(i).id, mStats.get(i).kills));
		}
		
		lineChart1.getData().addAll(frames, moves, kites);
		lineChart2.getData().addAll(hp, kills);

		pane.setCenter(lineChart1);
		pane2.setCenter(lineChart2);
		
		splitPane.setOrientation(Orientation.VERTICAL);
		splitPane.getItems().addAll(pane, pane2);

		tab.setContent(splitPane);
		tabPane.getTabs().add(tab);
	}
	
	private void setMatchStats(String ga_name){
		StarCraftBW_FileThread fileThread = new StarCraftBW_FileThread();
		fileThread.setFilePath_mStats(ga_name + "_stats");
		StarCraftBW_MatchStats matchStats = fileThread.getSavedMatchStats();
		try{
			this.mStats = matchStats.getMatchStats();
		}catch(NullPointerException np){
			this.mStats.clear();
		}
		
		fileThread.stopMe();
	}
	
	public static void main(String[] args) {
		launch();
	}
}
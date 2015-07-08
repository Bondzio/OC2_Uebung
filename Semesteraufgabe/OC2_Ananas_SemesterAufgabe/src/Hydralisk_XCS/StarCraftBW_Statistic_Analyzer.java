package Hydralisk_XCS;

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

import java.util.ArrayList;

public class StarCraftBW_Statistic_Analyzer extends Application{
	
	private ArrayList<StarCraftBW_MatchStat> mStats = new ArrayList<StarCraftBW_MatchStat>();
    private final String[] LEARNING_GA = new String[]{"ga1", "ga2","ga3","ga4"};
    private LineChart<Number, Number> frameChart;
    private String actualGA;

    private TabPane tabPane = new TabPane();

	@Override
	public void start(Stage stage) {

		stage.setTitle("ANANAS VultureAI");

		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
		this.frameChart = new LineChart<Number, Number>(xAxis, yAxis);

		for(int ga = 0; ga < LEARNING_GA.length; ga++){
			this.actualGA = LEARNING_GA[ga];
			setMatchStats();

			if(!mStats.isEmpty())
				createLineCharts();
		}
		createFrameChart();


		Scene scene = new Scene(tabPane, 1000, 800);
		stage.setScene(scene);
		stage.show();
	}

	private void createFrameChart(){
		BorderPane pane = new BorderPane();
		pane.setCenter(this.frameChart);
		Tab tab = new Tab();
		tab.setText("Frame Comparing");
		tab.setContent(pane);
		tabPane.getTabs().add(tab);
	}

	private void createLineCharts(){
		SplitPane splitPane = new SplitPane();

		Tab tab = new Tab();
		tab.setText(this.actualGA);

		BorderPane pane = new BorderPane();
		BorderPane pane2 = new BorderPane();

		pane.setCenter(getGraph1());
		pane2.setCenter(getGraph2());

		splitPane.setOrientation(Orientation.VERTICAL);
		splitPane.getItems().addAll(pane, pane2);

		tab.setContent(splitPane);
		tabPane.getTabs().add(tab);
	}

	private LineChart<Number, Number> getGraph1(){
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();

		LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		xAxis.setLabel("Executions");
		lineChart.setTitle("Learning Progress");

		XYChart.Series<Number, Number> frameSeries = addSeries(this.actualGA);
		XYChart.Series<Number, Number> frames = addSeries("Frames");
		XYChart.Series<Number, Number> moves = addSeries("AttackMoves");
		XYChart.Series<Number, Number> kites = addSeries("Kites");

		for(int i = 0; i < mStats.size(); i++){
			frameSeries.getData().add(new XYChart.Data<Number, Number>(mStats.get(i).id, mStats.get(i).frames));
			frames.getData().add(new XYChart.Data<Number, Number>(mStats.get(i).id, mStats.get(i).frames));
			moves.getData().add(new XYChart.Data<Number, Number>(mStats.get(i).id, mStats.get(i).countAttackMove));
			kites.getData().add(new XYChart.Data<Number, Number>(mStats.get(i).id, mStats.get(i).countKite));
		}
		lineChart.getData().addAll(frames, moves, kites);
		this.frameChart.getData().add(frameSeries);

		return lineChart;
	}

	private LineChart<Number, Number> getGraph2(){
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();

		LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		XYChart.Series<Number, Number> hp = addSeries("HealthPoints");
		XYChart.Series<Number, Number> kills = addSeries("Kills");

		for(int i = 0; i < mStats.size(); i++){
			hp.getData().add(new XYChart.Data<Number, Number>(mStats.get(i).id, mStats.get(i).hp));
			kills.getData().add(new XYChart.Data<Number, Number>(mStats.get(i).id, mStats.get(i).kills));
		}
		lineChart.getData().addAll(hp, kills);

		return lineChart;
	}

	private XYChart.Series<Number, Number> addSeries(String name){
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName(name);
		return series;
	}

	private void setMatchStats(){
		StarCraftBW_FileThread fileThread = new StarCraftBW_FileThread();
		fileThread.setFilePath_mStats(this.actualGA + "_stats");
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
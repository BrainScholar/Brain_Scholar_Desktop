package sample;

import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.CacheHint;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.util.Duration;

import java.awt.*;

public class Controller {
    public Button calcButton;
    public Label  chartLabel;
    public Slider gna, gk, beta, gamma, v_stim, c;
    public LineChart<Number,Number> lineChart;
    public NumberAxis xAxis, yAxis;
    public Animation graphAnimation;
    public int iteration;
    public double GNA, GK, BETA, GAMMA, V_STIM, C;
    public Timeline timeline = new Timeline();


    public void updateGraph(){
        //*******VARIABLE LABEL************//
        chartLabel.setText("GNA ="+gna.getValue()+" GK = "+gk.getValue()+" BETA = "+beta.getValue()+
                " GAMMA = "+ gamma.getValue()+" V STIM = "+ v_stim.getValue()+ " C = " + c.getValue());
        //********************************//

        //Declare a new series and assign it to the graph
        yAxis.setLowerBound(-5);
        yAxis.setUpperBound(5);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(10000);
        yAxis.setAutoRanging(false);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(2000);
        lineChart.setAnimated(false);
        ObservableList<XYChart.Series<Number, Number>> observable = FXCollections.observableArrayList();
        final XYChart.Series<Number,Number> series =  new XYChart.Series<>();




        //******************GET DATA******************//
        GNA = gna.getValue();
        GK = gk.getValue();
        BETA = beta.getValue();
        GAMMA = gamma.getValue();
        V_STIM = v_stim.getValue();
        C = c.getValue();
        //********************************************//


        //**********DECLARE VARIABLES (CONT.)**********//
        //Declare array sizes
        double[] f = new double[10000];
        double[] u = new double[10000];
        //"v" array is declared final to access within class that appends graph data
        //"v" array is all Y values for graph
        final double[] v = new double[10000];
        double del_t = 0.001;
        int cl = 30;
        int T = cl * 4;
        Double num = T / del_t;
        u[0] = -1.1;
        v[0] = -1.2;
        iteration = 0;
        //*********************************************//



        //*************CALCULATE AND GRAPH*************//
        timeline.getKeyFrames()
                .add(new KeyFrame(Duration.millis(2), (ActionEvent actionEvent) -> {
                        double floor = iteration / 3000;
                        double stinum = Math.floor(floor);
                        Double stimt = 3000 + 3000 * (stinum - 1);
                        Integer intstim = stimt.intValue();

                        f[iteration%6000] = v[iteration%6000] * (1 - ((v[iteration%6000] * v[iteration%6000]) / 3));
                        v[(iteration + 1)%6000] = 1 / C * (GNA * f[iteration%6000] - GK * u[iteration%6000]) * del_t + v[iteration%6000];
                        if (intstim.equals(iteration)) {
                            v[(iteration + 1)%6000] = v[(iteration + 1)%6000] + V_STIM;
                        }
                        u[(iteration + 1)%6000] = (v[iteration%6000] + BETA - GAMMA * u[iteration%6000]) * del_t + u[iteration%6000];
                        series.getData().add(new XYChart.Data<Number, Number>(iteration, v[iteration%6000]));
                        if(iteration > 10001) {
                            series.getData().remove(0);
                        }
                        if(iteration > 10000) {
                            xAxis.setLowerBound(xAxis.getLowerBound() + 1);
                            xAxis.setUpperBound(xAxis.getUpperBound() + 1);
                            System.out.println(iteration);
                        }
                        iteration++;

                }));
        //********************************************//

        //Gets rid of the circles on the thousands of points. Cleans up the graph.
        lineChart.setCreateSymbols(false);
        lineChart.setCache(true);
        lineChart.setCacheHint(CacheHint.SPEED);
        lineChart.getData().clear();
        timeline.setCycleCount(graphAnimation.INDEFINITE);
        timeline.play();
        observable.add(series);
        lineChart.getData().addAll(observable);
    }
}

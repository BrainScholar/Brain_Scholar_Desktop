package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.text.DecimalFormat;

public class Controller {
    public Button calcButton;
    public Label  chartLabel, descActivities;
    public Slider gna, gk, beta, gamma, v_stim, c;
    public NumberAxis xAxis, yAxis;
    public LineChart<Number,Number> lineChart;
    private double GNA, GK, BETA, GAMMA, V_STIM, C;
    private int iteration;
    public void updateGraph(){

        //Declare a new series and assign it to the graph
        XYChart.Series<Number, Number> series =  new XYChart.Series<Number, Number>();
        series.setName("Action Potential Data Series");
        yAxis.setLowerBound(-95);
        yAxis.setUpperBound(15);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(10000);
        yAxis.setAutoRanging(false);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(2000);
        lineChart.getData().addAll(series);


        //**********DECLARE VARIABLES (CONT.)**********//
        //Declare array sizes
        double[] f = new double[6000];
        double[] u = new double[6000];
        //"v" array is declared final to access within class that appends graph data
        //"v" array is all Y values for graph
        final double[] v = new double[6000];
        double del_t = 0.001;
        int cl = 30;
        int T = cl * 4;
        Double num = T / del_t;
        u[0] = -1.1;
        v[0] = -1.2;
        iteration = 0;
        //*********************************************//
        //******************GET DATA******************//
        GNA = gna.getValue();
        GK = gk.getValue();
        BETA = beta.getValue();
        GAMMA = gamma.getValue();
        V_STIM = v_stim.getValue();
        C = c.getValue();
        DecimalFormat df= new DecimalFormat("#.###");
        chartLabel.setText("GNA ="+ df.format(gna.getValue())+" GK = "+df.format(gk.getValue())+" BETA = "+df.format(beta.getValue())+
                " GAMMA = "+ df.format(gamma.getValue())+" V STIM = "+ df.format(v_stim.getValue())+ " C = " + df.format(c.getValue()));
        descActivities.setText("Graph generating");
        //********************************************//

        new Thread(()->{
            try {

                calcButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //******************RESET DATA******************//
                        GNA = gna.getValue();
                        GK = gk.getValue();
                        BETA = beta.getValue();
                        GAMMA = gamma.getValue();
                        V_STIM = v_stim.getValue();
                        C = c.getValue();
                        DecimalFormat df= new DecimalFormat("#.###");
                        chartLabel.setText("GNA ="+ df.format(gna.getValue())+" GK = "+df.format(gk.getValue())+
                                " BETA = "+df.format(beta.getValue())+" GAMMA = "+ df.format(gamma.getValue())+
                                " V STIM = "+ df.format(v_stim.getValue())+ " C = " + df.format(c.getValue()));
                        //********************************************//
                    }
                });
                for (int i = 0; true; i = (i + 1)%6000) {      // condition is always true so the graph
                    // would flow until app terminates
                    double floor = i / 3000;
                    double stinum = Math.floor(floor);
                    Double stimt = 3000 + 3000 * (stinum - 1);
                    Integer intstim = stimt.intValue();

                    // The equations for FitzHugh-Nagumo dynamic system are calculated on time unit i
                    // Mod function used to simulate circular array
                    // Size 6000 found out (after trial and error) to be ideal size even for low spec android devices
                    f[i] = v[i] * (1 - ((v[i] * v[i]) / 3));
                    v[(i + 1) % 6000] = 1 / C * (GNA * f[i] - GK * u[i]) * del_t + v[i];
                    if (intstim.equals(i)) {
                        v[(i + 1) % 6000] = v[(i + 1) % 6000] + V_STIM;
                    }
                    u[(i + 1) % 6000] = (v[i] + BETA - GAMMA * u[i]) * del_t + u[i];
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            double currentMin= -2.2, currentMax=2.2, minScaled=-90, maxScaled=10, scaledValue;
                            scaledValue= (maxScaled-minScaled)*(v[iteration%6000]-currentMin)/(currentMax-currentMin)+minScaled;
                            series.getData().add(new XYChart.Data<Number, Number>(iteration, scaledValue));
                            if(iteration > 10000) {
                                series.getData().remove(0);
                            }
                            if(iteration > 10000) {
                                xAxis.setLowerBound(xAxis.getLowerBound() + 1);
                                xAxis.setUpperBound(xAxis.getUpperBound() + 1);
                            }
                        }
                    });
                    iteration++;
                    Thread.sleep(1);
                }

            }
            catch (InterruptedException e){
               // e.printStackTrace();
            }
        }).start();
    }
}
package sample;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class Controller {
    public Button calcButton;
    public Label  chartLabel;
    public Slider gna, gk, beta, gamma, v_stim, c;
    public LineChart<Number,Number> lineChart;
    public int iteration;
    public double GNA, GK, BETA, GAMMA, V_STIM, C;

    public void updateGraph(){
        //*******VARIABLE LABEL************//
        chartLabel.setText("GNA ="+gna.getValue()+" GK = "+gk.getValue()+" BETA = "+beta.getValue()+
                " GAMMA = "+ gamma.getValue()+" V STIM = "+ v_stim.getValue()+ " C = " + c.getValue());
        //********************************//


        //Declare a new series and assign it to the graph
        XYChart.Series<Number, Number> series =  new XYChart.Series<Number, Number>();


        //******************GET DATA******************//
        GNA = gna.getValue()/10;
        GK = gk.getValue()/10;
        BETA = beta.getValue()/10;
        GAMMA = gamma.getValue()/10;
        V_STIM = v_stim.getValue()/10;
        C = c.getValue()/1000;
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
        for (int i = 0; i < 9998; i++) {
            double floor = i / 3000;
            double stinum = Math.floor(floor);
            Double stimt = 3000 + 3000 * (stinum - 1);
            Integer intstim = stimt.intValue();
            //increment constantly to reflect the onward march of time
            iteration++;

            f[i] = v[i] * (1 - ((v[i] * v[i]) / 3));
            v[i + 1] = 1 / C * (GNA * f[i] - GK * u[i]) * del_t + v[i];
            if (intstim.equals(i)) {
                v[i + 1] = v[i + 1] + V_STIM;
            }
            u[i + 1] = (v[i] + BETA - GAMMA * u[i]) * del_t + u[i];

            series.getData().add(new XYChart.Data<Number, Number>(iteration, v[/*iteration % 6000*/iteration]));
        }
        //********************************************//


        lineChart.getData().clear();
        lineChart.getData().addAll(series);
    }
}

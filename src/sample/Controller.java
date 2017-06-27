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
    public void updateGraph(){

        chartLabel.setText("GNA ="+gna.getValue()+" GK = "+gk.getValue()+" BETA = "+beta.getValue()+
                " GAMMA = "+ gamma.getValue()+" V STIM = "+ v_stim.getValue()+ " C = " + c.getValue());
        XYChart.Series<Number, Number> series =  new XYChart.Series<Number, Number>();
        for(double x=0; x<=3*Math.PI; x+=Math.PI *1/8){
            series.getData().add(new XYChart.Data<Number,Number>(x,Math.sin(x)));
            System.out.println(x+"  "+ Math.sin(x));
        }
        lineChart.getData().clear();
        lineChart.getData().addAll(series);
    }
}

package sample;

import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.awt.*;
import java.text.DecimalFormat;

public class Controller {
    public VBox sliderMenu, graphBox;
    public Button calcButton, someOtherModuleButton, fhnModuleButton;
    public Label  chartLabel, gnaValue, gkValue, betaValue, gammaValue, v_stimValue, cValue, stimRateValue;
    public Slider gna, gk, beta, gamma, v_stim, c, stimRate;
    public LineChart<Number,Number> lineChart;
    public NumberAxis xAxis, yAxis;
    public Animation graphAnimation;
    public int iteration, STIMRATE;
    public double GNA, GK, BETA, GAMMA, V_STIM, C;
    public Timeline timeline = new Timeline();
    public ObservableList<XYChart.Series<Number, Number>> observable;
    public XYChart.Series<Number,Number> series;
    //Use this format to set the amount of decimal places we want to show on our x-axis(time in this case) as we go
    public DecimalFormat df2 = new DecimalFormat(".##");

    public void initialize(){
        //*********************************GRAPH SETUP*********************************//
        //Set the boundaries of the viewport
        yAxis.setLowerBound(-100);
        yAxis.setUpperBound(20);
        yAxis.setTickUnit(10);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(10000);
        //Turn off auto ranging so that it doesn't zoom and scale weird
        yAxis.setAutoRanging(false);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        //We make our x-axis/time values visible per 2000 values (or 2ms)
        xAxis.setTickUnit(2000);

        //*****************LABEL FORMATTER*************************//
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            //We adjust our x values to milliseconds by dividing by 1000 or multiplying by 0.001
            //Then, we add our decimal format of 2 places (declared above) and add "ms" to the end
            @Override
            public String toString(Number object) {
                return df2.format(object.doubleValue() * 0.001) + "ms";
            }

            @Override
            public Number fromString(String string) {
                return 0;
            }
        });
        //********************************************************//

        lineChart.setAnimated(false);
        //Declare a new series and assign it to the graph
        ObservableList<XYChart.Series<Number, Number>> observable = FXCollections.observableArrayList();
        lineChart.setStyle(".chart-series-line");
        final XYChart.Series<Number,Number> series =  new XYChart.Series<>();
        //**************************************************************************//



        //*********************SLIDER LABELS*************************//
        //We set the text in the labels to the formatted slider values
        gnaValue.setText(String.format("%.2f", gna.getValue()));
        gkValue.setText(String.format("%.2f", gk.getValue()));
        betaValue.setText(String.format("%.2f", beta.getValue()));
        gammaValue.setText(String.format("%.2f", gamma.getValue()));
        v_stimValue.setText(String.format("%.2f", v_stim.getValue()));
        cValue.setText(String.format("%.3f", c.getValue()));
        stimRateValue.setText(String.format("%.2f", stimRate.getValue()/1000));

        //With this method, we change the text in the label with every value change of the slider
        gna.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                gnaValue.setText(String.format("%.2f", gna.getValue()));
            }
        });

        gk.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                gkValue.setText(String.format("%.2f", gk.getValue()));
            }
        });

        beta.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                betaValue.setText(String.format("%.2f", beta.getValue()));
            }
        });

        gamma.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                gammaValue.setText(String.format("%.2f", gamma.getValue()));
            }
        });

        v_stim.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                v_stimValue.setText(String.format("%.2f", v_stim.getValue()));
            }
        });

        c.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                cValue.setText(String.format("%.3f", c.getValue()));
            }
        });

        stimRate.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                stimRateValue.setText(String.format("%.2f", stimRate.getValue()/1000));
            }
        });
        //***************************************************//

        //*****************MODULE BUTTONS********************//
        //In here we can set what happens when the user clicks on the module buttons.
        someOtherModuleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                graphBox.setVisible(false);
                sliderMenu.setVisible(false);
            }
        });


        fhnModuleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                graphBox.setVisible(true);
                sliderMenu.setVisible(true);
            }
        });
    }


    public void updateGraph(){
        observable = FXCollections.observableArrayList();
        series =  new XYChart.Series<>();
        //*****************CHANGE VALUES*********************//
        calcButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Here, we pass the values of all the sliders to the variables above when the user clicks the "Calculate" button.
                //This overrides the previous values graphed
                GNA = gna.getValue();
                GK = gk.getValue();
                BETA = beta.getValue();
                GAMMA = gamma.getValue();
                V_STIM = v_stim.getValue();
                C = c.getValue();
                STIMRATE = (int) stimRate.getValue();
            }
        });
        //*************************************************//


        //******************GET DATA******************//
        GNA = gna.getValue();
        GK = gk.getValue();
        BETA = beta.getValue();
        GAMMA = gamma.getValue();
        V_STIM = v_stim.getValue();
        C = c.getValue();
        STIMRATE = (int) stimRate.getValue();
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
                    calcButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //***********GET DATA***********//
                            //Get data from sliders
                            GNA = gna.getValue();
                            GK = gk.getValue();
                            BETA = beta.getValue();
                            GAMMA = gamma.getValue();
                            V_STIM = v_stim.getValue();
                            C = c.getValue();
                            STIMRATE = (int) stimRate.getValue();
                            //*****************************//
                        }
                    });

                    // The equations for FitzHugh-Nagumo dynamic system are calculated on time unit i
                    // Mod function used to simulate circular array
                    // Size 6000 found out (after trial and error) to be ideal size even for low spec android devices
                    double floor = iteration / STIMRATE;
                    double stinum = Math.floor(floor);
                    Double stimt = STIMRATE + STIMRATE * (stinum - 1);
                    Integer intstim = stimt.intValue();

                    f[iteration%6000] = v[iteration%6000] * (1 - ((v[iteration%6000] * v[iteration%6000]) / 3));
                    v[(iteration + 1)%6000] = 1 / C * (GNA * f[iteration%6000] - GK * u[iteration%6000]) * del_t + v[iteration%6000];
                    System.out.println(STIMRATE);
                    if (iteration%STIMRATE == 0) {
                        v[(iteration + 1)%6000] = v[(iteration + 1)%6000] + V_STIM;
                        System.out.println(STIMRATE);
                    }
                    u[(iteration + 1)%6000] = (v[iteration%6000] + BETA - GAMMA * u[iteration%6000]) * del_t + u[iteration%6000];
                    double currentMin= -2.2, currentMax=2.2, minScaled=-90, maxScaled=10, scaledValue;
                    scaledValue= (maxScaled-minScaled)*(v[iteration%6000]-currentMin)/(currentMax-currentMin)+minScaled;
                    series.getData().add(new XYChart.Data<Number, Number>(iteration, scaledValue));
                    if(iteration > 10001) {
                        //After we reach 10000 iterations, it will remove the earliest data point with each new one
                        series.getData().remove(0);
                    }
                    if(iteration > 10000) {
                        //We adjust the viewport with every new point
                        xAxis.setLowerBound(xAxis.getLowerBound() + 1);
                        xAxis.setUpperBound(xAxis.getUpperBound() + 1);
                    }
                    iteration++;

                }));
        //********************************************//

        //Gets rid of the circles on the thousands of points. Cleans up the graph.
        lineChart.setCreateSymbols(false);
        lineChart.setCache(true);
        lineChart.setCacheHint(CacheHint.SPEED);
        lineChart.getData().clear();
        //Animation goes on forever and ever, Amen.
        timeline.setCycleCount(graphAnimation.INDEFINITE);
        timeline.play();
        observable.add(series);
        lineChart.getData().addAll(observable);
    }
}

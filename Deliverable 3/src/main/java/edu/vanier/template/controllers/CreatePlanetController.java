package edu.vanier.template.controllers;

import edu.vanier.template.ui.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;

/**
 * FXML controller  for the start page.
 *
 * @author Fadi Rasmy
 */
public class CreatePlanetController {
    @FXML
    private Circle circle1;
    @FXML
    private Circle circle2;
    @FXML
    private Circle circle3;
    @FXML
    private Circle circle4;
    @FXML
    private Circle circle5;


    @FXML
    private Button customButton1;
    @FXML
    private Button customButton2;
    @FXML
    private Button customButton3;
    @FXML
    private Button customButton4;
    @FXML
    private Button customButton5;

    @FXML
    private Sphere planet;





    @FXML
    public void initialize() {
        customButton1.setOnAction(event -> handleStartButton());
    }
    // I'm not sure but here its probably going to be with the scene controller to change scene
    public  void handleStartButton(){
        MainApp.switchScene(MainApp.VOID_SIMULATION_LAYOUT);
    }
}
package edu.vanier.template.controllers;
import edu.vanier.template.ui.MainApp;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;

/**
 * FXML controller  for the start page.
 *
 * @author Fadi Rasmy
 */
public class VoidSimulationController {
    @FXML
    private Circle Circle;
    @FXML
    private Button customButton;

    @FXML
    public void initialize() {
        customButton.setOnAction(event -> handleStartButton());
    }
    // I'm not sure but here its probably going to be with the scene controller to change scene
    public  void handleStartButton(){
        MainApp.switchScene(MainApp.CREATE_PLANET_LAYOUT);
    }
}
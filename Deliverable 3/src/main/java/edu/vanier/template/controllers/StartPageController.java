package edu.vanier.template.controllers;

import edu.vanier.template.ui.MainApp;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.Button;

/**
 * FXML controller  for the start page.
 *
 * @author Joseph Josue Forestal
 */
public class StartPageController {
    @FXML
    private Label labelTitle;
    @FXML
    private Button buttonStart;

    @FXML
    public void initialize() {
        buttonStart.setOnAction(event -> handleStartButton());
    }
    // I'm not sure but here its probably going to be with the scene controller to change scene
    public  void handleStartButton(){
        MainApp.switchScene(MainApp.TEMPLATE_SELECTION_LAYOUT);
    }

}


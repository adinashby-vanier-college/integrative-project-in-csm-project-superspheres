package edu.vanier.template.controllers;

import edu.vanier.template.ui.MainApp;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SimulationsController {
    @FXML
    private VBox vBoxRootNode;
    @FXML
    private StackPane stackPaneNode;

    @FXML
    private Button buttonAddPlanet;

    //SubScene
    private SubScene subScene;
    private static Group groupRoot;
    @FXML
    public void initialize(){
        groupRoot = new Group();
        subScene = new SubScene(groupRoot,800,600);
        subScene.setFill(Color.BLACK);
        stackPaneNode.getChildren().add(subScene);
        buttonAddPlanet.setOnAction(event -> {handleStartButton();});
        initializeBinding();
    }
    public  void handleStartButton(){
        MainApp.switchScene(MainApp.CREATE_PLANET_LAYOUT);
    }
    public  void initializeBinding(){
        if (subScene != null){
            subScene.widthProperty().bind(vBoxRootNode.widthProperty());
            subScene.heightProperty().bind(vBoxRootNode.heightProperty());
            stackPaneNode.prefWidthProperty().bind(vBoxRootNode.widthProperty());
            stackPaneNode.prefHeightProperty().bind(vBoxRootNode.heightProperty());

            buttonAddPlanet.layoutXProperty().bind(subScene.widthProperty().multiply(0.40));
            buttonAddPlanet.layoutYProperty().bind(subScene.heightProperty().multiply(0.90));
        }
    }

    public  void  loadAssets(String templateName){
        //add logic
    }
}

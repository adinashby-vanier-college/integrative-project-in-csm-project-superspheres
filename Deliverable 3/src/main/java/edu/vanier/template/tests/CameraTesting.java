package edu.vanier.template.tests;

import edu.vanier.template.sim.CameraControlsHandler;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

import javax.swing.*;

public class CameraTesting extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root,800,600,true);

        scene.setFill(Color.BLACK);
        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);
        scene.setCamera(perspectiveCamera);

        perspectiveCamera.setNearClip(0.01);
        perspectiveCamera.setFarClip(100000);
        perspectiveCamera.setTranslateZ(-10);

        Box box = new Box(100,10,8);

        root.getChildren().add(box);


        CameraControlsHandler cameraControlsHandler = new CameraControlsHandler(perspectiveCamera);

        cameraControlsHandler.handleCamera(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Camera movement testing");

        primaryStage.show();


    }
}

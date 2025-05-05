package edu.vanier.template.helpers;

import edu.vanier.template.controllers.SimulationMainPageController;
import edu.vanier.template.controllers.TemplateSelectionController;
import edu.vanier.template.sim.Body;
import edu.vanier.template.sim.CameraControlsHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ListViewBodies {
    private final static Logger logger = LoggerFactory.getLogger(ListViewBodies.class);
    private Body sphere;
    private Sphere shape3DCloned;
    String bodyName;

    Text3D text3D;
    public HBox hBoxEntry;
    public  static  ListView<HBox> listView = SimulationMainPageController.getLastInstance().listViewSimulatedBodies;
    public ListViewBodies(String bodyName, Body bodyToClone){
        this.bodyName = bodyName;
        this.sphere = bodyToClone;
        this.setCloneShape(bodyToClone);
        if(listView != null) logger.info("List view was created");
        listView.getItems().add(this.createBodyEntries());

    }
    public HBox createBodyEntries(){
        sphere.getTrail().setActive(false);
        HBox hBoxEntryContainer = new HBox(15);
        hBoxEntryContainer.setAlignment(Pos.CENTER_LEFT);
        hBoxEntryContainer.setPadding(new Insets(8,15,8,15));
        hBoxEntryContainer.setStyle("-fx-background-color: rgba(50,50,50,0.7); -fx-background-radius: 8");

        /*Label labelName = new Label(this.bodyName);
        labelName.setStyle("-fx-text-fill: white; -fx-font-size: 14");
        labelName.setMinWidth(100);
        */
        //Name controls
        HBox hBoxNameControls = new HBox(8);
        CheckBox checkBoxShowName = new CheckBox();
        Label labelShowName = new Label("✍");


        TextField textFieldName = new TextField(this.bodyName);
        textFieldName.setStyle("-fx-text-fill: white; -fx-font-size: 14 " );
        textFieldName.setPrefWidth(60);
        textFieldName.setPrefHeight(20);

        hBoxNameControls.getChildren().addAll(this.shape3DCloned,labelShowName,textFieldName, checkBoxShowName);



        checkBoxShowName.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            logger.info("Planet name " + sphere.getName());
            sphere.setShowName(newValue);
        }));


        //}
        // Spacers
        Region spacer = new Region();
        Region spacer1 = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        //Stats controls
        HBox hboxStatsControls = new HBox(8);
        hboxStatsControls.setAlignment(Pos.CENTER);
        CheckBox checkBoxStats = new CheckBox();
        Label labelStatsIcon = new Label("\uD83D\uDCCA");
        labelStatsIcon.setStyle("-fx-font-size: 16");
        hboxStatsControls.getChildren().addAll(checkBoxStats,  labelStatsIcon);

        //Trails controls
        HBox hBoxTrailsControls = new HBox(8);
        hBoxTrailsControls.setAlignment(Pos.CENTER_RIGHT);

        CheckBox trailCheckBox = new CheckBox("Trail");
        trailCheckBox.selectedProperty().addListener((obs,wasSelected,isSelected)->{
            this.sphere.getTrail().setActive(isSelected);

            if(!isSelected && sphere.getTrail() != null){
                Group  parent = (Group) sphere.getParent();
                if(parent!= null){
                    parent.getChildren().removeAll(sphere.getTrail().getTrailParticles());
                }
                sphere.getTrail().getTrailParticles().clear();
            }
        });

        trailCheckBox.setSelected(false);
        trailCheckBox.setStyle("-fx-text-fill: white");

        //Color indicator o see whats the color of the trails
        Circle circleTrailColorIndicator = new Circle(8);
        circleTrailColorIndicator.setFill(Color.WHITE);
        circleTrailColorIndicator.setStroke(Color.WHITE);
        circleTrailColorIndicator.setStrokeWidth(1.5);

        //Our color picker this can also be used for non texturize planet:

        ColorPicker colorPicker = new ColorPicker(Color.WHITE);
        colorPicker.setStyle("-fx-color-label-visible: false");
        colorPicker.setVisible(false);

        circleTrailColorIndicator.setOnMouseClicked(e->{
         colorPicker.setVisible(!colorPicker.isVisible());
         if(colorPicker.isVisible()) colorPicker.show();
        });


        // Listener for the color property

        colorPicker.valueProperty().addListener((obs, oldColor, newColor) ->{
            circleTrailColorIndicator.setFill(newColor);
            sphere.getTrail().setTrailColor(newColor);
            //i will add the logic for trails if I have time here
        });



        hBoxTrailsControls.getChildren().addAll(trailCheckBox, circleTrailColorIndicator,colorPicker);

        hBoxEntryContainer.getChildren().addAll(hBoxNameControls,spacer1,hboxStatsControls,spacer,hBoxTrailsControls);
        
        hBoxEntryContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        CameraControlsHandler cameraHandler = SimulationMainPageController.getLastInstance().cameraControlsHandler;
                        System.out.println("now following" + sphere.getName());
                        cameraHandler.setFocused(true);
                        cameraHandler.setFollowBody(sphere);
                    }
                }
            }
        });

        // remove button:
       Button removeButton = new Button("X");
       removeButton.setStyle("-fx-background-color:  #ff4444; -fx-text-fill: white");
       removeButton.setOnAction(e->{
           sphere.removeBody();
       });
       hBoxEntryContainer.getChildren().add(removeButton);
       this.hBoxEntry =hBoxEntryContainer;
        return hBoxEntryContainer;
        
    }



    public void setCloneShape(Body bodyToClone){
        this.shape3DCloned = new Sphere(12);
        bodyToClone.materialProperty().addListener(((observable, oldValue, newValue) -> {
            this.shape3DCloned.setMaterial((PhongMaterial) newValue);
        }));
    }
}

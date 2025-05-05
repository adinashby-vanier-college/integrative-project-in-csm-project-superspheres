package edu.vanier.template.helpers;

import edu.vanier.template.math.Vector3D;
import edu.vanier.template.sim.Body;
import edu.vanier.template.sim.BodyHandler;
import edu.vanier.template.sim.CameraControlsHandler;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.event.ActionEvent;


import java.awt.*;

public class DragAndDropSystem {
    private final static Logger logger = LoggerFactory.getLogger(DragAndDropSystem.class);
    private TilePane tilePane;
    private  Group targetGroup;
    private Body draggedObject;
    private double dragOffSetX, dragOffSetY ;
    private HBox hBoxToolBar;
    private double sceneWidth, sceneHeight;
    private double setBackX, setBackY;
    private SubScene subScene;
    private javafx.scene.control.TextField textFieldVelocity;
    private VBox vboxInputVelocity;
    private BodyHandler bodyHandler;
    private  boolean isDraggable = true;
    private  CameraControlsHandler cameraControlsHandler;
    private Vector3D vector3DInitialPosition;
    private  RayCaster rayCaster;
    public  DragAndDropSystem(TilePane tilePane, Group targetGroup, SubScene subScene, CameraControlsHandler cameraControlsHandler, HBox toolBar, TextField textField , VBox vboxInputVelocity, BodyHandler bodyHandler){
        this.tilePane = tilePane;
        this.targetGroup = targetGroup;
        this.hBoxToolBar = toolBar;
        this.cameraControlsHandler = cameraControlsHandler;
        this.subScene = subScene;
        this.textFieldVelocity  = textField;
        this.vboxInputVelocity = vboxInputVelocity;
        this.bodyHandler = bodyHandler;
        this.rayCaster = new RayCaster(targetGroup, cameraControlsHandler);

        tilePane.getChildren().addListener((ListChangeListener<? super Node>) change->{
            while(change.next()){
                for(Node node : change.getAddedSubList()){
                    if(node instanceof Body){
                        setUpBodyDrag(node);
                    }
                }
            }
        });
        setTilePaneEventConsumers();

    }

    public  void setTilePaneEventConsumers(){
        if (this.tilePane == null) return;
        else {
            this.tilePane.setPickOnBounds(false);
            for(int i = 0; i < tilePane.getChildren().size(); i++){
                if (tilePane.getChildren().get(i) instanceof Sphere) {
                    tilePane.getChildren().get(i).setPickOnBounds(true);
                    setUpBodyDrag(tilePane.getChildren().get(i));
                }
            }
        }
    }

    public  void startDrag(Body body, MouseEvent event){

            draggedObject = body;
            dragOffSetX = event.getSceneX() - body.getTranslateX();
            dragOffSetY = event.getSceneY() - body.getTranslateY();


    }


    public void transferToSimulation(){
        if(draggedObject != null && cameraControlsHandler != null){
            ScalePlanet.prepareForSimulation(draggedObject);
            this.targetGroup.getChildren().add(draggedObject);
            this.putObjectInFrontOfCamera(draggedObject,500);
            Body cloneDragged = draggedObject;
            this.DropHandler(cloneDragged);
            draggedObject = null;
        }
    }
    public void updateDragPosition(MouseEvent event){
        if(!isDraggable) return;
        double newX = event.getSceneX() - dragOffSetX;
        double newY = event.getSceneY() - dragOffSetY;

        draggedObject.setTranslateX(newX);
        draggedObject.setTranslateY(newY);

        transferToSimulation();

    }
    public void setUpBodyDrag(Node node){
        if(node instanceof Body body){
            body.setOnMousePressed(e->{
                if(e.getButton() == MouseButton.PRIMARY){
                    startDrag(body, e);
                    e.consume();

                }
            });

            body.setOnMouseDragged(e->{
                if (draggedObject != null && cameraControlsHandler != null && tilePane.getChildren().contains(draggedObject)){
                    updateDragPosition(e);
                }
            });
        }
    }
    public void DragHandler(){


    }
    public void DropHandler(Body draggedObject1){
        if(this.bodyHandler == null) return;
        vboxInputVelocity.setVisible(true);
        vboxInputVelocity.setManaged(true);
        textFieldVelocity.setVisible(true);
        textFieldVelocity.setManaged(true);


            //Find central body if looking at 1
        RayCaster.RayCastResult hit = rayCaster.castRay(1000000000);
        if(hit != null && hit.hitBody != null) {
            logger.info("hit body is "+ hit.hitBody.toString());
            logger.info("distance" + hit.distance);
            double defaultVelocity = draggedObject1.vOrbital(hit.hitBody,hit.distance);
            logger.info("velocty "+defaultVelocity);
            textFieldVelocity.setText(String.format("%.2f", defaultVelocity));
        }

            this.textFieldVelocity.setOnKeyPressed(e->{
                try {
                    if (e.getCode() == KeyCode.ENTER) {
                        Point3D lookVector = cameraControlsHandler.getLookVector();
                        double velocityValue = Double.parseDouble(textFieldVelocity.getText());
                        Point3D velocityVector = lookVector.multiply(velocityValue);
                        Vector3D vector3DVelocity = new Vector3D(velocityVector.getX(), velocityVector.getY(), velocityVector.getZ());

                        logger.info("key pressed");
                        draggedObject1.setPosition(vector3DInitialPosition);
                        draggedObject1.setVelocity(vector3DVelocity);
                        bodyHandler.add(draggedObject1);
                        this.textFieldVelocity.setVisible(false);
                        this.textFieldVelocity.setManaged(false);
                        this.textFieldVelocity.setText("0.00");
                        this.textFieldVelocity.getParent().getParent().requestFocus();

                        this.vboxInputVelocity.setVisible(false);
                        this.vboxInputVelocity.setManaged(false);
                    }

                }catch (Exception exception){
                    logger.info( exception.getMessage()+"Exception");
                }
            });

    }

    public void putObjectInFrontOfCamera(Shape3D shape3D, double distanceFromCamera) {
        PerspectiveCamera perspectiveCamera = cameraControlsHandler.getCamera();
       Point3D point3DCameraPosition = targetGroup.sceneToLocal(perspectiveCamera.localToScene(0, 0, 0));
       // Point3D point3DCameraPosition = new Point3D(perspectiveCamera.getTranslateX(), perspectiveCamera.getTranslateY(), perspectiveCamera.getTranslateZ());
        Point3D point3DLookVector = cameraControlsHandler.getLookVector();

        // Get the radius if the shape is a Sphere
        double radius = 0;
        if (shape3D instanceof Sphere) {
            radius = ((Sphere)shape3D).getRadius();
        }

        // Calculate spawn position accounting for object's radius
        Point3D point3DSpawnPosition = point3DCameraPosition.add(
                point3DLookVector.multiply(distanceFromCamera).add(radius,-radius,0)
        );

        if(shape3D != null) {
            shape3D.setTranslateX(point3DSpawnPosition.getX());
            shape3D.setTranslateY(point3DSpawnPosition.getY());
            shape3D.setTranslateZ(point3DSpawnPosition.getZ());
            vector3DInitialPosition = new Vector3D(
                    shape3D.getTranslateX(),
                    shape3D.getTranslateY(),
                    shape3D.getTranslateZ()
            );
        }
    }
    public void DragAndDropHandler(){
        transferToSimulation();
    }
}

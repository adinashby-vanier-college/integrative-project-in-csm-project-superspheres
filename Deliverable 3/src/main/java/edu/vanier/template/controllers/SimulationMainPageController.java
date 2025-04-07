package edu.vanier.template.controllers;

import edu.vanier.template.helpers.*;
import edu.vanier.template.math.Vector3D;
import edu.vanier.template.sim.BodyHandler;
import edu.vanier.template.sim.CameraControlsHandler;
import edu.vanier.template.sim.Planet;
import edu.vanier.template.helpers.SavenLoad;

import edu.vanier.template.sim.SolarSystemAssets;
import edu.vanier.template.ui.MainApp;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import static edu.vanier.template.helpers.SavenLoad.loadFileSaver;
import static edu.vanier.template.helpers.SavenLoad.writePlanetsToFile;

public class SimulationMainPageController {

    // All the main containers:
    @FXML
    private VBox vboxMainRootNode;
    @FXML
    private AnchorPane anchorPaneMainRootNode;

    //Containers with necessaryComponents:
    @FXML
    private VBox vboxAddPlanetButton;
    //Components:
    @FXML
    private Button buttonAddPlanet;

    @FXML
    private HBox hboxRootToolBar;
    // Components:
    @FXML
    private AnchorPane anchorPaneToolBarKit;
    @FXML
    private TitledPane tiltPanePlanets;
    @FXML
    private TilePane tilePanePlanets;
    @FXML
    private VBox vBoxCustomizeButton;
    @FXML
    private Button buttonCustomizePlanet;

    @FXML
    private VBox vboxCameraIcon;
    //Components(Essential):
    @FXML
    private Button buttonCamera;

    @FXML
    private Button buttonExit;
    @FXML
    private VBox vboxExitButton;

    @FXML
    private Button buttonSettings;
    @FXML
    private VBox vboxSetting;
    @FXML
    private VBox vboxSettingButton;
    @FXML
    private AnchorPane anchorPaneSetting;
    @FXML
    private Button buttonSettingExit;
    @FXML
    private Button buttonSettingLoad;
    @FXML
    private Button buttonSettingSave;

    @FXML
    private VBox vboxPlanetStatistic;
    //Components(Essentials):
    @FXML
    private Label labelStatisticPositionValue;
    @FXML
    private Label labelStatisticVelocityValue;
    @FXML
    private Label labelStatisticPlanetName;
    @FXML
    private VBox vboxCameraControls;
    //Components(essentials):
    @FXML
    private Button buttonSetBackOrigin;
    @FXML
    private TextField textFieldCameraSpeed;

    //Subscene simulation
    @FXML
    private SubScene subSceneSimulation;

    @FXML
    private Slider simulationSpeedSlider;
    @FXML
    private TextField textFieldVelocity;
    
    //Non fxml field members:
    private Group groupRootNode = new Group();
    
    private PerspectiveCamera camera = new PerspectiveCamera(true);
    private CameraControlsHandler cameraControlsHandler;
    
    private AnimationTimer animationTimer;
    private long prevTime = System.nanoTime();

    private BodyHandler bodyHandler;
    private DragAndDropSystem dragAndDropSystem ;
    private SolarSystemAssets solarSystemAssets;
    private GarbageCollector garbageCollector;

   static SimulationMainPageController currentInstance;
    public  SimulationMainPageController(){
        currentInstance = this;
    }
    public  static SimulationMainPageController getLastInstance(){return currentInstance;}

    public void loadTemplate(String templateName){
        if(garbageCollector == null){
            garbageCollector = new GarbageCollector(groupRootNode, bodyHandler);
        }
        garbageCollector.clearAll();

        switch (templateName){
            case "empty":
                //nothing cuz its empty
            case "solarSystem":
                if(solarSystemAssets == null){
                    solarSystemAssets =  new SolarSystemAssets();
                }
                solarSystemAssets.loadAssets(groupRootNode,bodyHandler);
                break;

        }
    }
    double count = 1.0;
    double count2 = 1.0;
    
    double timeConstant = 1;

    public void handlerButtonSetting() {
        if (vboxSetting.isVisible()) {
            vboxSetting.setVisible(false);
            buttonSettingSave.setManaged(false);
            buttonSettingExit.setManaged(false);
            buttonSettingLoad.setManaged(false);
        }else {
            // animations can be added here
            vboxSetting.setVisible(true);
            buttonSettingSave.setManaged(true);
            buttonSettingExit.setManaged(true);
            buttonSettingLoad.setManaged(true);
        }
    }


    public void handlerButtonAddPlanetEvent() {
        if (!vboxAddPlanetButton.isVisible()) {
            return;
        }
        // animations can be added here
        hboxRootToolBar.setVisible(true);
        vboxAddPlanetButton.setVisible(false);
        vboxAddPlanetButton.setManaged(false);
        this.tiltPanePlanets.setExpanded(true);
    }

    //add event to exit button
    public  void handleExitButton(){
        MainApp.switchScene(MainApp.TEMPLATE_SELECTION_LAYOUT);
    }
    public  void handleSelectionButton(){
        MainApp.switchScene(MainApp.CREATE_PLANET_LAYOUT);
    }



    public void handlerTitlePaneEvent() {
        this.tiltPanePlanets.expandedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.2625));
                pauseTransition.setOnFinished(s -> {
                    tiltPanePlanets.setExpanded(false);
                    hboxRootToolBar.setVisible(false);
                    hboxRootToolBar.setManaged(false);
                    vboxAddPlanetButton.setVisible(true);
                    vboxAddPlanetButton.setManaged(true);
                });
                pauseTransition.play();

            }
        });
    }

    public void handlerCameraButtonEvent() {
        //animations logic to be added
        this.buttonCamera.setOnAction(e -> {
            if (!this.vboxCameraControls.isVisible()) {
                this.vboxCameraControls.setVisible(true);
                this.vboxCameraControls.setManaged(true);
            } else {
                this.vboxCameraControls.setVisible(false);
                this.vboxCameraControls.setManaged(false);
            }

        });
    }

    public void initializeBinding() {
        this.tiltPanePlanets.setExpanded(false);
        this.vboxCameraControls.setManaged(false);
        this.vboxCameraControls.setVisible(false);


        if (this.subSceneSimulation == null || this.vboxMainRootNode == null) {
            return;
        }

        vboxMainRootNode.sceneProperty().addListener((obs, oldScene, newScene) -> {
            this.vboxMainRootNode.prefWidthProperty().bind(newScene == null ? oldScene.widthProperty() : newScene.widthProperty());
            this.vboxMainRootNode.prefHeightProperty().bind(newScene == null ? oldScene.heightProperty() : newScene.heightProperty());

            this.anchorPaneMainRootNode.prefWidthProperty().bind(newScene == null ? oldScene.widthProperty() : newScene.widthProperty());
            this.anchorPaneMainRootNode.prefHeightProperty().bind(newScene == null ? oldScene.heightProperty() : newScene.heightProperty());

            this.subSceneSimulation.widthProperty().bind(newScene == null ? oldScene.widthProperty() : newScene.widthProperty());
            this.subSceneSimulation.heightProperty().bind(newScene == null ? oldScene.heightProperty() : newScene.heightProperty());

            this.hboxRootToolBar.prefWidthProperty().bind(newScene == null ? oldScene.widthProperty() : newScene.widthProperty());

            this.anchorPaneToolBarKit.prefWidthProperty().bind((newScene == null ? oldScene.widthProperty() : newScene.widthProperty()));


            this.tiltPanePlanets.prefWidthProperty().bind(hboxRootToolBar.prefWidthProperty());
            this.tilePanePlanets.prefWidthProperty().bind((hboxRootToolBar.prefWidthProperty()));

        });

        vboxMainRootNode.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            vboxAddPlanetButton.setLayoutX(newWidth.doubleValue() - 150);

        });
        vboxMainRootNode.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            vboxAddPlanetButton.setLayoutY(newHeight.doubleValue() - 150);
            hboxRootToolBar.setLayoutY(newHeight.doubleValue() * 0.88);
        });
        
        textFieldCameraSpeed.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                Double speed = Double.parseDouble(textFieldCameraSpeed.getText());
                cameraControlsHandler.setMaxSpeed(speed);
                cameraControlsHandler.setAcceleration(speed/4);
                textFieldCameraSpeed.getParent().requestFocus();
            }
        });
        
        simulationSpeedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            timeConstant = newVal.doubleValue();
        });

    }
    public void handlerSaveButtonEvent(){

        try {
            // Write to file
            writePlanetsToFile(bodyHandler.getBodies(), "src/main/resources/planets.txt");
            System.out.println("Planet data successfully written to file.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    public void HandlerSettingLoad() throws IOException {
        loadFileSaver(groupRootNode,bodyHandler,"src/main/resources/planets.txt");
    }

    public void handleCamera() {
        Box box = new Box(25, 25, 20);
        box.setMaterial(new PhongMaterial(Color.RED));
        groupRootNode.getChildren().add(box);

        this.camera.setTranslateZ(-1000);
        this.camera.setFarClip(100000);
        this.camera.setNearClip(0.1);

        this.subSceneSimulation.setRoot(groupRootNode);

        this.subSceneSimulation.setCamera(this.camera);
        cameraControlsHandler = new CameraControlsHandler(camera);

        this.subSceneSimulation.sceneProperty().addListener((obs, oldScene, newScene) -> {
            cameraControlsHandler.setMovementAllow(true);
            cameraControlsHandler.handleCamera(subSceneSimulation.getScene());
        });
    }

    public void setSubSceneSimulation() {
        this.subSceneSimulation.setFill(Color.BLACK);
        this.subSceneSimulation.setCamera(this.camera);
        this.subSceneSimulation.setDepthTest(DepthTest.ENABLE);
    }
    
    public void setupAnimationTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double dt = (now - prevTime) / 1E9;
                cameraControlsHandler.updateMovement(dt);
                RotationClass.updateAllRotations(dt);
                bodyHandler.update(dt * timeConstant);
                prevTime = now;
            }
        };
    }

    public void setupBodies(){
        bodyHandler = new BodyHandler();

        dragAndDropSystem = new DragAndDropSystem(tilePanePlanets,this.groupRootNode,this.subSceneSimulation, cameraControlsHandler,hboxRootToolBar, textFieldVelocity,bodyHandler);
        dragAndDropSystem.DragAndDropHandler();

        Planet planet = new Planet(new Vector3D(-650 , 0, .01), new Vector3D(0, 0, -12.28), 100.0, 10);

        Planet planet2 = new Planet(new Vector3D(0  , 0, 0), new Vector3D(0,0,0),10000, 170);

        Planet planet3 = new Planet(new Vector3D(0  , 0, 0), new Vector3D(0,0,0),150, 170);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        Rotate yRotate2 = new Rotate(0, Rotate.Y_AXIS)
                ;

        planet.getTransforms().add(yRotate);
        planet2.getTransforms().add(yRotate2);
AnimationTimer animationTimer1 = new AnimationTimer() {
    @Override
    public void handle(long now) {
        count += 10;
        count2 += 0.05;
        yRotate.setAngle(count);
        yRotate2.setAngle(count2);

        if (count >= 360 ) count = 0;
    }
};
animationTimer1.start();

        BuildInBodies buildInBodies = new BuildInBodies(planet);
        BuildInBodies buildInBodies1 = new BuildInBodies(planet2);

        buildInBodies.applyTextures("Mercury");
        buildInBodies1.applyTextures("Sun");
      //  buildInBodies2.applyTextures("Sun");

        groupRootNode.getChildren().add(planet);
        groupRootNode.getChildren().add(planet2);



       bodyHandler.add(planet);
        bodyHandler.add(planet2);

    }
    public void spawnPlanet(double x1,double y1, double z1,double x2,double y2, double z2, int mass, int size,int angle, String texture){

        Planet planet = new Planet(new Vector3D(x1 , y1, z1), new Vector3D(x2, y2, x2), mass, size);
        Rotate yRotate = new Rotate(angle, Rotate.Y_AXIS);
        planet.getTransforms().add(yRotate);
        Rotate yRotate2 = new Rotate(0, Rotate.Y_AXIS);
        AnimationTimer animationTimer1 = new AnimationTimer() {
            @Override
            public void handle(long now) {
                count += 10;
                count2 += 0.05;
                yRotate.setAngle(count);
                yRotate2.setAngle(count2);

                if (count >= 360 ) count = 0;
            }
        };
        animationTimer1.start();
        BuildInBodies buildInBodies = new BuildInBodies(planet);
        buildInBodies.applyTextures(texture);
        groupRootNode.getChildren().add(planet);
        bodyHandler.add(planet);

    }

    public void handleCreationButton() {
        MainApp.switchScene(MainApp.CREATE_PLANET_LAYOUT);
        spawnPlanet(650, 0, .01, 0, 0, 12.28, 100, 10, 0, "earth");
        System.out.println(cameraControlsHandler.getPrevMovementVector());
    }


    public void setTilePanePlanets(){
        tilePanePlanets.setPickOnBounds(false);
    }
    @FXML
    public void initialize() {

        Planet mercury = new Planet(
                new Vector3D(-800-600, 0, 0),
                new Vector3D(0, 0, 0),
                10,    // Mass
                20     // Size
        );


        // Venus
        Planet venus = new Planet(
                new Vector3D(-1200-600, 0, 0),
                new Vector3D(0, 0, 0),
                20,    // Mass
                40     // Size
        );

        // Earth
        Planet earth = new Planet(
                new Vector3D(-1600-600, 0, 0),
                new Vector3D(0, 0, 0),
                20,    // Mass
                42     // Size
        );
        BuildInBodies buildInBodies = new BuildInBodies(mercury);
        BuildInBodies buildInBodies1 = new BuildInBodies(venus);
        BuildInBodies buildInBodies2 = new BuildInBodies(earth);

        buildInBodies.applyTextures("Mercury");
        buildInBodies1.applyTextures("Venus");
        buildInBodies2.applyTextures("Earth");

        tilePanePlanets.getChildren().addAll(earth,mercury,venus);


        groupRootNode.setDepthTest(DepthTest.ENABLE);

        vboxSettingButton.setLayoutX(200);

        //make sure that the setting button sticks to the top right corner
        AnchorPane.setTopAnchor(vboxSettingButton, 20.0);     // Stick to top
        AnchorPane.setRightAnchor(vboxSettingButton, 20.0);   // Stick to right
        AnchorPane.setTopAnchor(vboxSetting, 20.0);     // Stick to top
        AnchorPane.setRightAnchor(vboxSetting, 40.0);   // Stick to right


        // Button events
        this.buttonAddPlanet.setOnAction(e -> {
            handlerButtonAddPlanetEvent();
        });

        buttonExit.setOnAction(event -> handleExitButton());
        buttonSettings.setOnAction(event -> handlerButtonSetting());
        //buttonCustomizePlanet.setOnAction(event -> handleCreationButton());
        buttonSettingExit.setOnAction(event -> handleExitButton());

        buttonSettingSave.setOnAction(event -> handlerSaveButtonEvent());
        buttonSettingLoad.setOnAction(event -> {
            try {
                HandlerSettingLoad();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        //Handler
        handlerCameraButtonEvent();
        handlePlanetCreationButtonEvent();
        initializeBinding();
        setSubSceneSimulation();
        handlerTitlePaneEvent();
        handleCamera();



        setupBodies();
        setupAnimationTimer();
        animationTimer.start();
    }

    public void handlePlanetCreationButtonEvent() {
        CreatePlanetController createPlanetController = new CreatePlanetController();
        createPlanetController.setSimulationController(this);

        buttonCustomizePlanet.setOnAction(event -> {
            try {
                Parent root = FxUIHelper.loadFXML("planetCreation_layout", createPlanetController);

                Scene currentScene = buttonCustomizePlanet.getScene();
                double width = currentScene.getWidth();
                double height = currentScene.getHeight();

                Scene scene = new Scene(root, width, height);
                Stage stage = new Stage();
                stage.setTitle("Planet Creation");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(buttonCustomizePlanet.getScene().getWindow());
                stage.showAndWait();
            } catch (Exception e) {
                System.out.println("AAAAAAAAA");
                e.getMessage();
            }
        });
    }

    public TilePane getTilePanePlanets() {
        return tilePanePlanets;
    }
}

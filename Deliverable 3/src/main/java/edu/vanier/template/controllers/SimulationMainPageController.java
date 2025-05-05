package edu.vanier.template.controllers;

import edu.vanier.template.helpers.*;
import edu.vanier.template.math.Vector3D;
import edu.vanier.template.sim.BodyHandler;
import edu.vanier.template.sim.CameraControlsHandler;
import edu.vanier.template.sim.Planet;

import edu.vanier.template.sim.SolarSystemAssets;
import edu.vanier.template.ui.MainApp;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static edu.vanier.template.helpers.SavenLoad.loadFileSaver;
import static edu.vanier.template.helpers.SavenLoad.writePlanetsToFile;

public class SimulationMainPageController {
    private final static Logger logger = LoggerFactory.getLogger(SimulationMainPageController.class);

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
    private Button buttonCustomizePlanet;

    @FXML
    private VBox vboxCameraIcon;
    //Components(Essential):
    @FXML
    private Button buttonCamera;
    @FXML
    private Button buttonSimulationSpeed;
    @FXML
    private VBox vboxSimulationSpeed;

    @FXML
    private Button buttonSettings;
    @FXML
    private VBox vboxSetting;
    @FXML
    private VBox vboxSettingButton;
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
    @FXML
    private VBox vboxInputVelocity;
    @FXML
    private  Button buttonSimulatedBodies;
    @FXML
    private VBox vBoxSimulatedBodies;
    @FXML
    public  ListView<HBox> listViewSimulatedBodies;
    @FXML
    private Button buttonPause;
    @FXML
    private Button buttonPlay;


    //Non fxml field members:
    private Group groupRootNode = new Group();
    
    private PerspectiveCamera camera = new PerspectiveCamera(true);
    public CameraControlsHandler cameraControlsHandler;
    
    private AnimationTimer animationTimer;
    private long prevTime = System.nanoTime();
    private long prevTimeRotation = System.nanoTime();

    private BodyHandler bodyHandler;
    private SolarSystemAssets solarSystemAssets;
    private GarbageCollector garbageCollector;

   static SimulationMainPageController currentInstance;
    public  SimulationMainPageController(){
        currentInstance = this;
    }
    public  static SimulationMainPageController getLastInstance(){return currentInstance;}
    public PerspectiveCamera getCamera(){return this.camera;}
    public Group getGroupRootNode(){return  this.groupRootNode;}
    public BodyHandler getBodyHandler(){return this.bodyHandler;}
    public void loadTemplate(String templateName){
        pauseSimulation();
        if(garbageCollector == null){
            garbageCollector = new GarbageCollector(groupRootNode, bodyHandler);
        }
        groupRootNode.getChildren().clear();
        RotationClass.clearAllRotations();
        bodyHandler.getBodies().clear();
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
            case "asteroidBelt":

                    if(solarSystemAssets == null){
                        solarSystemAssets =  new SolarSystemAssets();
                    }
                    solarSystemAssets.loadAsteroidBelts(groupRootNode,bodyHandler);
                    break;
        }
        unPauseSimulation();
    }
    double count = 1.0;
    double count2 = 1.0;

    double timeConstant = 10;

    public void handlerButtonSetting() {
        if (vboxSetting.isVisible()) {
            vboxSetting.setVisible(false);
            buttonSettingSave.setManaged(false);
            buttonSettingExit.setManaged(false);
            buttonSettingLoad.setManaged(false);
        }else {
            // animations can be added here
            vboxSetting.setVisible(true);
            if (buttonSettingSave != null)buttonSettingSave.setManaged(true);
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
        groupRootNode.getChildren().clear();
        bodyHandler.getBodies().clear();
        vboxSetting.setVisible(false);
        vboxSetting.setManaged(false);
        this.vboxCameraControls.setVisible(false);
        this.vboxCameraControls.setManaged(false);
        this.vboxSimulationSpeed.setVisible(false);
        this.vboxSimulationSpeed.setManaged(false);
        tiltPanePlanets.setExpanded(false);
        vBoxSimulatedBodies.setVisible(false);

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
                this.vboxSimulationSpeed.setVisible(false);
                this.vboxSimulationSpeed.setManaged(false);
            } else {
                this.vboxCameraControls.setVisible(false);
                this.vboxCameraControls.setManaged(false);
            }

        });
    }

    public void handlerSimulationSpeedButtonEvent() {
        //animations logic to be added
        this.buttonSimulationSpeed.setOnAction(e -> {
            if (!this.vboxSimulationSpeed.isVisible()) {
                this.vboxSimulationSpeed.setVisible(true);
                this.vboxSimulationSpeed.setManaged(true);
                this.vboxCameraControls.setVisible(false);
                this.vboxCameraControls.setManaged(false);
            } else {
                this.vboxSimulationSpeed.setVisible(false);
                this.vboxSimulationSpeed.setManaged(false);
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
        tilePanePlanets.setHgap(5);
        tilePanePlanets.setVgap(5);
        tilePanePlanets.setPadding(new Insets(5));

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
        this.camera.setTranslateZ(-1000);
        this.camera.setFarClip(2000000000);
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
                bodyHandler.update(dt * timeConstant);
                prevTime = now;

                // if some see th
                //if(true){
                    bodyHandler.getBodies().forEach(body -> {
                        body.updateLabelPosition();
                    });
                //}
            }
        };
    }

    public void pauseSimulation() {
        animationTimer.stop();
    }

    public void unPauseSimulation() {
        prevTime = System.nanoTime();
        animationTimer.start();
    }

    public void setupBodies(){
        bodyHandler = new BodyHandler();

        DragAndDropSystem dragAndDropSystem = new DragAndDropSystem(tilePanePlanets, this.groupRootNode, this.subSceneSimulation, cameraControlsHandler, hboxRootToolBar, textFieldVelocity, vboxInputVelocity, bodyHandler);
        dragAndDropSystem.DragAndDropHandler();

        Planet planet = new Planet(new Vector3D(-650 , 0, .01), new Vector3D(0, 0, -12.28), 100.0, 10);

        Planet planet2 = new Planet(new Vector3D(0  , 0, 0), new Vector3D(0,0,0),10000, 170);

        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        Rotate yRotate2 = new Rotate(0, Rotate.Y_AXIS)
                ;

        planet.getTransforms().add(yRotate);
        planet2.getTransforms().add(yRotate2);
AnimationTimer animationTimer1 = new AnimationTimer() {
    @Override
    public void handle(long now) {
        double dt = (now - prevTimeRotation) / 1E9;
        RotationClass.updateAllRotations(dt);

        count += 10;
        count2 += 0.05;
        yRotate.setAngle(count);
        yRotate2.setAngle(count2);

        prevTimeRotation = now;
        if (count >= 360 ) count = 0;
    }
};
animationTimer1.start();

        BuildInBodies buildInBodies = new BuildInBodies(planet);
//        BuildInBodies buildInBodies1 = new BuildInBodies(planet2);
      //  buildInBodies2.applyTextures("Sun");
      
      planet.removeBody();
      planet2.removeBody();
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

    public void setTilePanePlanets(){
        tilePanePlanets.setPickOnBounds(false);
    }
    @FXML
    public void initialize() {
        groupRootNode.setDepthTest(DepthTest.ENABLE);
        vboxSettingButton.setLayoutX(200);
        simulationSpeedSlider.setValue(10);



        // Button events
        this.buttonAddPlanet.setOnAction(e -> {
            handlerButtonAddPlanetEvent();
        });
        buttonPause.setOnAction(event -> pauseSimulation());
        buttonPlay.setOnAction(event -> unPauseSimulation());
        buttonSettings.setOnAction(event -> handlerButtonSetting());
        buttonSettingExit.setOnAction(event -> handleExitButton());
        buttonSimulatedBodies.setOnAction(event -> {handleButtonSimulatedBodies();});
        buttonSetBackOrigin.setOnAction(event -> cameraControlsHandler.reset());

        buttonSettingSave.setOnAction(event -> {
            System.out.println("save soon");
            handlerSaveButtonEvent();
        });
        buttonSettingLoad.setOnAction(event -> {
            try {
                garbageCollector.clearAll();
                HandlerSettingLoad();
                pauseSimulation();
                unPauseSimulation();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //Handler
        handlerCameraButtonEvent();
        handlerSimulationSpeedButtonEvent();
        initializeBinding();
        setSubSceneSimulation();
        handlerTitlePaneEvent();
        handleCamera();
        handlePlanetCreationButtonEvent();

        setupBodies();
        //Initalize scale :

        setupAnimationTimer();
        animationTimer.start();
    }

    public  void handleButtonSimulatedBodies(){
        logger.info("Make vbox visible");
        if(!vBoxSimulatedBodies.isVisible()) {
            vBoxSimulatedBodies.setVisible(true);
            vBoxSimulatedBodies.setManaged(true);
        }else {
            vBoxSimulatedBodies.setVisible(false);
            vBoxSimulatedBodies.setManaged(false);
        }

    }

    public void handlePlanetCreationButtonEvent() {
        CreatePlanetController createPlanetController = new CreatePlanetController();
        createPlanetController.setSimulationController(this);

        buttonCustomizePlanet.setOnAction(event -> {
            try {
                pauseSimulation();
                Parent root = FxUIHelper.loadFXML("planetCreation_layout", createPlanetController);

                Scene currentScene = buttonCustomizePlanet.getScene();
                double width = currentScene.getWidth();
                double height = currentScene.getHeight();

                Scene scene = new Scene(root, width, height);
                Stage planetCreationStage = new Stage();
                planetCreationStage.setTitle("Planet Creation");
                planetCreationStage.setScene(scene);
                planetCreationStage.initModality(Modality.APPLICATION_MODAL);
                planetCreationStage.initOwner(buttonCustomizePlanet.getScene().getWindow());
                planetCreationStage.setOnHidden(e -> unPauseSimulation());
                planetCreationStage.showAndWait();
            } catch (Exception e) {
                System.out.println("AAAAAAAAA");
            }
        });
    }

    public TilePane getTilePanePlanets() {
        return tilePanePlanets;
    }
}

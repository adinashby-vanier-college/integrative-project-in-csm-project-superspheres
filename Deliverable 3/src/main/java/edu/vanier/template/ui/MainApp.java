package edu.vanier.template.ui;

import edu.vanier.template.controllers.*;
import edu.vanier.template.helpers.FxUIHelper;
import java.io.IOException;
import java.util.logging.Level;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a JavaFX project template to be used for creating GUI applications.
 *
 * The JavaFX GUI framework (version: 22.0.2) is linked to this project in the
 * build.gradle file.
 * @link: https://openjfx.io/javadoc/22/
 * @see: /Build Scripts/build.gradle
 * @author frostybee.
 */
public class MainApp extends Application {

    // The FXML file name of the primary scene.
    public static final String START_SCENE = "StartPage_layout";
    public static final String MAIN_SCENE = "MainApp_layout";
    // The FXML file name of the secondary scene.
    public static final String TEMPLATE_SELECTION_LAYOUT = "TemplateSelection_layout";
    //declare the FXML file for the void scene
    public static final String VOID_SIMULATION_LAYOUT = "VoidSimulation_layout";
    //declare the FXML file for the planet creation scene
    public static final String CREATE_PLANET_LAYOUT = "planetCreation_layout";

    public static  final String SIMULATIONS_LAYOUT = "Simulations_layout";
    public static final String SIMULATION_MAIN_PAGE_LAYOUT = "SimulationMainPage_layout";

    private final static Logger logger = LoggerFactory.getLogger(MainApp.class);
    private static Scene scene;
    private static SceneController sceneController;

    @Override
    public void stop() {
        // TODO: 
        // Here, we need to perform teardown operations such as stopping running 
        // animation, etc.
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Bootstrapping the application...");
            // Load the scene of the primary stage.
            Parent root = FxUIHelper.loadFXML(START_SCENE, new StartPageController());
            scene = new Scene(root);
            // Add the primary scene to the scene-switching controller.
            sceneController = new SceneController(scene);
            sceneController.addScene(START_SCENE, root);
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            primaryStage.setTitle("Super Spheres");
            // Request putting this appliation's main window on top of other
            // already-opened windows upon launching the app.
            primaryStage.setAlwaysOnTop(true);
            primaryStage.show();
            primaryStage.setAlwaysOnTop(false);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            java.util.logging.Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Switches between scenes based on the provided FXML file name. This method
     * checks the type of scene (primary or secondary) and either activates an
     * existing scene or loads the specified FXML scene for the first time and
     * adds it to the scene controller.
     *
     * @param fxmlFileName the name of the FXML file that represents the scene
     * to switch to.
     */
    public static void switchScene(String fxmlFileName) {
        try {
            if (fxmlFileName.equals(START_SCENE)) {
                    // No need to register the primary scene as it
                    // was already done in the start method.
                    sceneController.activateScene(fxmlFileName);
            }
            else if (fxmlFileName.equals(TEMPLATE_SELECTION_LAYOUT)) {
                    if (!sceneController.sceneExists(fxmlFileName)) {
                    // Instantiate the corresponding FXML controller if the 
                    // specified scene is being loaded for the first time.
                    TemplateSelectionController controller = new TemplateSelectionController();
                    Parent root = FxUIHelper.loadFXML(fxmlFileName, controller);
                    sceneController.addScene(TEMPLATE_SELECTION_LAYOUT, root);
                }
                // The scene has been previously added, we activate it.
                sceneController.activateScene(fxmlFileName);
            } else if (fxmlFileName.equals(MAIN_SCENE)) {
                if (!sceneController.sceneExists(fxmlFileName)) {
                    MainAppFXMLController controller = new MainAppFXMLController();
                    Parent root = FxUIHelper.loadFXML(fxmlFileName, controller);
                    sceneController.addScene(MAIN_SCENE, root);
                }
                sceneController.activateScene(fxmlFileName);
            }
         else if (fxmlFileName.equals(VOID_SIMULATION_LAYOUT)) {
            if (!sceneController.sceneExists(fxmlFileName)) {
                VoidSimulationController controller = new VoidSimulationController();
                Parent root = FxUIHelper.loadFXML(fxmlFileName, controller);
                sceneController.addScene(VOID_SIMULATION_LAYOUT, root);
            }
            sceneController.activateScene(fxmlFileName);
        }
            else if (fxmlFileName.equals(CREATE_PLANET_LAYOUT)) {
                if (!sceneController.sceneExists(fxmlFileName)) {
                    CreatePlanetController controller = new CreatePlanetController();
                    Parent root = FxUIHelper.loadFXML(fxmlFileName, controller);
                    sceneController.addScene(CREATE_PLANET_LAYOUT, root);
                }
                sceneController.activateScene(fxmlFileName);
            } else if (fxmlFileName.equals(SIMULATIONS_LAYOUT)) {
                if(!sceneController.sceneExists(fxmlFileName)){
                    SimulationsController controller = new SimulationsController();
                    Parent root = FxUIHelper.loadFXML(fxmlFileName, controller);
                    sceneController.addScene(SIMULATIONS_LAYOUT, root);
                }
                sceneController.activateScene(fxmlFileName);
            } else if (fxmlFileName.equals(SIMULATION_MAIN_PAGE_LAYOUT)) {
                if(!sceneController.sceneExists(SIMULATION_MAIN_PAGE_LAYOUT)){
                    SimulationMainPageController controller = new SimulationMainPageController();
                    Parent root = FxUIHelper.loadFXML(fxmlFileName, controller);
                    sceneController.addScene(SIMULATION_MAIN_PAGE_LAYOUT, root);
                }
                sceneController.activateScene(fxmlFileName);
            }


            //TODO: You can register or activate additional scenes here, 
            //      based on the logic used to add the secondary scene (as shown above).            
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

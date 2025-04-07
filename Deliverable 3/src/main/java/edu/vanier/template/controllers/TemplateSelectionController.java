package edu.vanier.template.controllers;

import edu.vanier.template.sim.Planet;
import edu.vanier.template.ui.MainApp;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML controller class for the secondary scene.
 *
 * @author frostybee
 */
public class TemplateSelectionController {

    private final static Logger logger = LoggerFactory.getLogger(TemplateSelectionController.class);
    @FXML
    BorderPane borderPane;
    @FXML
    Button btnTemplateSelection;
    @FXML
    Button returnToStart;
    @FXML
    Button btnTemplateSelection2;
    @FXML
    Button btnTemplateSelection3;
    @FXML
    ImageView emptySystemImage;
    @FXML
    ImageView asteroidSystemImage;
    @FXML
    ImageView solarSystemImage;

    private SimulationMainPageController simulationMainPageController;

    @FXML
    public void initialize() {
        logger.info("Initializing MainAppController...");
        btnTemplateSelection.setOnAction(this::loadVoidPage);
        btnTemplateSelection2.setOnAction(this::loadPrimaryScene);
        btnTemplateSelection3.setOnAction(this::loadSolarSystem);
        returnToStart.setOnAction(this::handleReturnToStartButton);
        initializeBinding();
    }

    private void loadPrimaryScene(Event e) {
        MainApp.switchScene(MainApp.MAIN_SCENE);
       // MainApp.switchScene(MainApp.SIMULATION_PAGE_LAYOUT);
        logger.info("Loaded the primary scene...");
    }
    private void loadVoidPage(Event e) {
       // MainApp.switchScene(MainApp.VOID_SIMULATION_LAYOUT);
        MainApp.switchScene(MainApp.SIMULATION_MAIN_PAGE_LAYOUT);
        logger.info("Loaded the void page scene...");
    }


    private void loadSolarSystem(Event e) {

        // Switch to the simulation scene
        MainApp.switchScene(MainApp.SIMULATION_MAIN_PAGE_LAYOUT);

        // Use Platform.runLater to ensure controller is created
        Platform.runLater(() -> {
            SimulationMainPageController controller = SimulationMainPageController.getLastInstance();
            if (controller != null) {
                controller.loadTemplate("solarSystem");
            } else {
                System.err.println("Failed to get simulation controller");
            }
        });
    }
    private void handleReturnToStartButton(Event e) {
        MainApp.switchScene(MainApp.START_SCENE);
    }

    private void initializeBinding() {
        emptySystemImage.fitWidthProperty().bind(borderPane.widthProperty().multiply(0.25));
        asteroidSystemImage.fitWidthProperty().bind(borderPane.widthProperty().multiply(0.25));
        solarSystemImage.fitWidthProperty().bind(borderPane.widthProperty().multiply(0.25));
        emptySystemImage.fitHeightProperty().bind(borderPane.heightProperty().multiply(0.4));
        asteroidSystemImage.fitHeightProperty().bind(borderPane.heightProperty().multiply(0.4));
        solarSystemImage.fitHeightProperty().bind(borderPane.heightProperty().multiply(0.4));


        btnTemplateSelection.prefWidthProperty().bind(emptySystemImage.fitWidthProperty());
        btnTemplateSelection2.prefWidthProperty().bind(asteroidSystemImage.fitWidthProperty());
        btnTemplateSelection3.prefWidthProperty().bind(solarSystemImage.fitWidthProperty());
        btnTemplateSelection.prefHeightProperty().bind(emptySystemImage.fitHeightProperty());
        btnTemplateSelection2.prefHeightProperty().bind(asteroidSystemImage.fitHeightProperty());
        btnTemplateSelection3.prefHeightProperty().bind(solarSystemImage.fitHeightProperty());
    }
}

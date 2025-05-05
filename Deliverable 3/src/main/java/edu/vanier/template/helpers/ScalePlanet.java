package edu.vanier.template.helpers;

import edu.vanier.template.sim.Body;
import javafx.scene.layout.TilePane;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScalePlanet {
    private static final double TILE_MIN_RADIUS = 15;  // Visual radius for smallest planets
    private static final double TILE_MAX_RADIUS = 55;  // Visual radius for largest planets

    // Stores original radii for all bodies
    private static final Map<Body, Double> originalRadii = new HashMap<>();


    /**
     * Adjusts a Body's display size for TilePane while preserving physics
     */
    public static void prepareForTilePane(Body body) {
        // Store original radius if not already stored
        if (!originalRadii.containsKey(body)) {
            originalRadii.put(body, body.getRadius());
        }

        // Calculate display radius (logarithmic scaling)
        double displayRadius = calculateDisplayRadius(body.getRadius());

        // Set the display size (this affects visual only)
        body.setRadius(displayRadius);
    }

    /**
     * Restores a Body to its original size for simulation
     */
    public static void prepareForSimulation(Body body) {
        if (originalRadii.containsKey(body)) {
            double original = originalRadii.get(body);
            body.setRadius(original); // Restore original physics size
            originalRadii.remove(body); // Clean up
        }
    }

    private static double calculateDisplayRadius(double actualRadius) {
        // Logarithmic scaling to compress range (10-600 -> 15-25)
        double ratio = Math.log1p(actualRadius / 50) / Math.log1p(600 / 50);
        return TILE_MIN_RADIUS + (TILE_MAX_RADIUS - TILE_MIN_RADIUS) * ratio;
    }
    /**
     * Configures TilePane for consistent planet display
     *
     */
    public static void configureTilePane(TilePane pane) {
        pane.setHgap(5);
        pane.setVgap(5);
        //pane.setPadding(new Insets(5));
        pane.setPrefColumns(5); // Adjust as needed
    }
}


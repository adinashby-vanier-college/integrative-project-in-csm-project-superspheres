package edu.vanier.template.sim;

import edu.vanier.template.helpers.BuildInBodies;
import edu.vanier.template.helpers.GarbageCollector;
import edu.vanier.template.helpers.RotationClass;
import edu.vanier.template.math.Vector3D;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SolarSystemAssets {

    private List<Planet> solarSystemBodies = new ArrayList<>();

    private  GarbageCollector garbageCollector;

    public void loadAssets(Group rootNode, BodyHandler bodyHandler) {
        garbageCollector = new GarbageCollector(rootNode, bodyHandler);
        Planet sun = new Planet(
                new Vector3D(0, 0, 0),
                new Vector3D(0, 0, 0),
                100000,
                600
        );
        BuildInBodies.applyTextures(sun, "Sun");

        // Mercury (closest to sun)
        Planet mercury = new Planet(
                new Vector3D(-800-600, 0, 0),
                new Vector3D(0, 0, 0),
                10,    // Mass
                20     // Size
        );
        mercury.setVelocity(new Vector3D(0,0,-mercury.vOrbital(sun,800+600)));
        BuildInBodies.applyTextures(mercury, "Mercury");

        // Venus
        Planet venus = new Planet(
                new Vector3D(-1200-600, 0, 0),
                new Vector3D(0, 0, 0),
                20,    // Mass
                40     // Size
        );
        venus.setVelocity(new Vector3D(0,0,-venus.vOrbital(sun,1200+600)));
        BuildInBodies.applyTextures(venus, "Venus");

        // Earth
        Planet earth = new Planet(
                new Vector3D(-1600-600, 0, 0),
                new Vector3D(0, 0, 0),
                20,    // Mass
                42     // Size
        );
        earth.setVelocity(new Vector3D(0,0,-earth.vOrbital(sun,1600+600)));
        BuildInBodies.applyTextures(earth, "Earth");

        // Mars
        Planet mars = new Planet(
                new Vector3D(-2200-600, 0, 0),
                new Vector3D(0, 0, 0),
                10,    // Mass
                30     // Size
        );
        BuildInBodies.applyTextures(mars, "Mars");
        mars.setVelocity(new Vector3D(0,0,-mars.vOrbital(sun,2200+600)));
        // Jupiter
        Planet jupiter = new Planet(
                new Vector3D(-3200-600, 0, 0),
                new Vector3D(0, 0, 0),
                200,   // Mass
                180    // Size
        );
        BuildInBodies.applyTextures(jupiter, "Jupiter");
        jupiter.setVelocity(new Vector3D(0,0,-jupiter.vOrbital(sun,3200+600)));
        // Saturn
        Planet saturn = new Planet(
                new Vector3D(-4200-600, 0, 0),
                new Vector3D(0, 0, 0),
                150,   // Mass
                150    // Size
        );
        saturn.setVelocity(new Vector3D(0,0,-saturn.vOrbital(sun,4200+600)));
        // No texture applied - will use default

        // Uranus
        Planet uranus = new Planet(
                new Vector3D(-5200-600, 0, 0),
                new Vector3D(0, 0, 0),
                100,   // Mass
                80     // Size
        );
       uranus.setVelocity(new Vector3D(0,0,-uranus.vOrbital(sun,5200+600)));
       BuildInBodies.applyTextures(uranus, "Uranus");

        // Neptune
        Planet neptune = new Planet(
                new Vector3D(-6200-600, 0, 0),
                new Vector3D(0, 0, 0),
                100,   // Mass
                75     // Size
        );
        // No texture applied - will use default
        neptune.setVelocity(new Vector3D(0,0,-neptune.vOrbital(sun,6200+600)));
        // Pluto
        Planet pluto = new Planet(
                new Vector3D(-7200-600, 0, 0),
                new Vector3D(0, 0, 0),
                1,     // Mass
                10     // Size
        );
        pluto.setVelocity(new Vector3D(0,0,-pluto.vOrbital(sun,7200+600)));


        // Add all bodies to lists
        solarSystemBodies.addAll(List.of(
                sun, mercury, venus, earth, mars,
                jupiter, saturn, uranus, neptune, pluto
        ));

        // Add to scene and physics
        rootNode.getChildren().addAll(solarSystemBodies);
        bodyHandler.addAll(sun, mercury, venus, earth, mars,
                jupiter, saturn, uranus, neptune, pluto);

        RotationClass rotationClass = new RotationClass();
        rotationClass.addBody(sun, 50);
        rotationClass.addBody(mercury, 120);
        rotationClass.addBody(venus, 90);
        rotationClass.addBody(earth, 80);
        rotationClass.addBody(mars, 70);
       rotationClass.addBody(jupiter, 50);
       rotationClass.addBody(saturn, 40);
        rotationClass.addBody(uranus, 30);
        rotationClass.addBody(neptune, 30);
        rotationClass.addBody(pluto, 20);
    }
}

package edu.vanier.template.helpers;
import edu.vanier.template.math.Vector3D;
import edu.vanier.template.sim.Body;
import edu.vanier.template.sim.Planet;
import edu.vanier.template.sim.BodyHandler;
import edu.vanier.template.sim.Star;
import javafx.scene.Group;
import javafx.scene.shape.Shape3D;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;



public class SavenLoad {

    public static void writePlanetsToFile(ArrayList<Body> planets, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Body planet : planets) {
                writer.write(planet.toSave());
                writer.newLine(); // Add a newline after each planet
            }
        }
    }


    public static void loadFileSaver(Group rootNode, BodyHandler bodyHandler, String file) throws IOException {

        ArrayList<Planet> planets = readPlanetsFromFile(file);
        RotationClass rotationClass = new RotationClass();
        Body sun = new Star(
                new Vector3D(0, 0, 0),
                new Vector3D(0, 0, 0),
                1000000, 600
        );
        sun.setName("Sun");
        BuildInBodies.applyTextures(sun, "Sun");
        rootNode.getChildren().add(sun);
        bodyHandler.add(sun);
        rotationClass.addBody(sun,50);


        //adding the assets to the system
        rootNode.getChildren().addAll(planets);
        for(Planet planet : planets){
            System.out.println("planet");
            bodyHandler.add(planet);
            String name = planet.getName();
            System.out.println(name);
            BuildInBodies.applyTextures(planet, name);
            rotationClass.addBody(planet, 50);
        }

    }


        public static ArrayList<Planet> readPlanetsFromFile(String filePath) throws IOException {
            ArrayList<Planet> planets = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line;
            while ((line = reader.readLine()) != null) {
                Planet planet = parsePlanetLine(line);
                if (planet != null) {
                    planets.add(planet);
                }
            }

            reader.close();
            return planets;
        }

        private static Planet parsePlanetLine(String line) {
            // Define regex patterns to extract the data
            Pattern positionPattern = Pattern.compile("position=Vector3D\\{x=([-\\d.]+), y=([-\\d.]+), z=([-\\d.]+)\\}");
            Pattern velocityPattern = Pattern.compile("velocity=Vector3D\\{x=([-\\d.]+), y=([-\\d.]+), z=([-\\d.]+)\\}");
            Pattern massPattern = Pattern.compile("mass=([-\\d.]+)");
            Pattern radiusPattern = Pattern.compile("mass=([-\\d.]+)");
            Pattern namePattern = Pattern.compile("name=([^,\\\\}]+)");

            // Extract position
            Matcher posMatcher = positionPattern.matcher(line);
            if (!posMatcher.find()) return null;
            Vector3D position = new Vector3D(
                    Double.parseDouble(posMatcher.group(1)),
                    Double.parseDouble(posMatcher.group(2)),
                    Double.parseDouble(posMatcher.group(3))
            );

            // Extract velocity
            Matcher velMatcher = velocityPattern.matcher(line);
            if (!velMatcher.find()) return null;
            Vector3D velocity = new Vector3D(
                    Double.parseDouble(velMatcher.group(1)),
                    Double.parseDouble(velMatcher.group(2)),
                    Double.parseDouble(velMatcher.group(3))
            );

            // Extract mass
            Matcher massMatcher = massPattern.matcher(line);
            if (!massMatcher.find()) return null;
            double mass = Double.parseDouble(massMatcher.group(1));

            // Extracting radius
            Matcher radiusMatcher = radiusPattern.matcher(line);
            if (!radiusMatcher.find()) return null;
            double radius = Double.parseDouble(massMatcher.group(1));

            //creating planet
            Planet planet = new Planet(position, velocity, mass, radius);

            // Extract name
            Matcher nameMatcher = namePattern.matcher(line);


            if (nameMatcher.find()) {
                String name = nameMatcher.group(1);
                planet.setName(name);
            } else {
                System.out.println("No match found.");
            }
            return planet;
        }
    }


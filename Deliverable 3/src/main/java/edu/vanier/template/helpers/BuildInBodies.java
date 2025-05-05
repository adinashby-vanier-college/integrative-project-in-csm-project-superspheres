package edu.vanier.template.helpers;

import edu.vanier.template.sim.Body;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.PhongMaterial;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;

/**
 * @author Josue
 */
public class BuildInBodies {
    private Body body;

    static HashMap<String, Image> bodyBuildTexture = new HashMap<>();


    public BuildInBodies(Body body) {
        setTextures();
        this.body = body;
    }


    public void applyTextures(String textureName) {
        if (textureName.equals("Earth")) {
            this.texturizeBody(bodyBuildTexture.get("Earth"), Color.BLUE);
        } else if (textureName.equals("Mars")) {
            this.texturizeBody(bodyBuildTexture.get("Mars"), Color.ORANGE);
        } else if (textureName.equals("Jupiter")) {
            this.texturizeBody(bodyBuildTexture.get("Jupiter"), Color.ORANGE);
        } else if (textureName.equals("Sun")) {
            this.texturizeBody(bodyBuildTexture.get("Sun"), Color.YELLOW);
        } else if (textureName.equals("Venus")) {
            this.texturizeBody(bodyBuildTexture.get("Venus"), Color.ORANGE);
        } else if (textureName.equals("Mercury")) {
            this.texturizeBody(bodyBuildTexture.get("Mercury"), Color.MEDIUMPURPLE);
        }

    }

    public void texturizeBody(Image image, Color color) {
        PhongMaterial phongMaterial = new PhongMaterial(color);
        phongMaterial.setBumpMap(image);
        phongMaterial.setDiffuseMap(image);
        phongMaterial.setSpecularMap(image);
        phongMaterial.setSelfIlluminationMap(image);
        this.body.setMaterial(phongMaterial);
    }

    public void setTextures() {
        // Earth texture
        // Image image = new Image(getClass().getResource("/BuildInBodiesImages/EarthImage.jpeg").toExternalForm());
        Image imageEarth = new Image("/fxml/BuildInBodiesImages/EarthImage.jpeg",256,256,true,true);
        Image imageMars = new Image("/fxml/BuildInBodiesImages/MarsImage.png",256,256,true,true);
        Image imageJupiter = new Image("/fxml/BuildInBodiesImages/JupiterImage.jpg",256,256,true,true);
        Image imageUranus = new Image("/fxml/BuildInBodiesImages/UranusImage.png",256,256,true,true);
        Image imageVenus = new Image("/fxml/BuildInBodiesImages/VenusImage.png",256,256,true,true);
        Image imageSun = new Image("/fxml/BuildInBodiesImages/SolarImage.jpg",256,256,true,true);
        Image imageMercury = new Image("/fxml/BuildInBodiesImages/MercuryImage.jpg",256,256,true,true);
        Image imageNeptune = new Image("/fxml/BuildInBodiesImages/NeptuneImage.jpg",256,256,true,true);
        Image imageSaturn = new Image("/fxml/BuildInBodiesImages/SaturnImage.jpg",256,256,true,true);
        Image imagePluto= new Image("/fxml/BuildInBodiesImages/PlutoImage.jpg",256,256,true,true);

        //Moons
        Image imageMoon = new Image("/fxml/BuildInBodiesImages/MoonImage.jpg",256,256,true,true);

        //Black hole:
        Image imageBlackHole = new Image("/fxml/BuildInBodiesImages/BlackHoleImage.jpg",256,256,true,true);
        bodyBuildTexture.put("Earth", imageEarth);
        bodyBuildTexture.put("Mars", imageMars);
        bodyBuildTexture.put("Jupiter", imageJupiter);
        bodyBuildTexture.put("Uranus", imageUranus);
        bodyBuildTexture.put("Venus", imageVenus);
        bodyBuildTexture.put("Sun", imageSun);
        bodyBuildTexture.put("Mercury", imageMercury);
        bodyBuildTexture.put("Neptune", imageNeptune);
        bodyBuildTexture.put("Saturn", imageSaturn);
        bodyBuildTexture.put("Pluto", imagePluto);

        //Moons
        bodyBuildTexture.put("Moon", imageMoon);


        //Black hole
        bodyBuildTexture.put("BlackHole", imageBlackHole);



    }

    public static void applyTextures(Shape3D planet, String textureName) {
        PhongMaterial material = new PhongMaterial();

        if (textureName != null && bodyBuildTexture.containsKey(textureName)) {
            Image image = bodyBuildTexture.get(textureName);
            // Set all maps properly(well since its the same iamge it does not add any depth to it but we can change it later)
            material.setDiffuseMap(image);       // Main texture
            material.setSpecularMap(image);      // Shiny highlights
            material.setBumpMap(image);          // Surface details
            material.setSelfIlluminationMap(image); // Glow effect

            // Set appropriate specular power and color based on planet type
            if ("Sun".equals(textureName)) {
                material.setSpecularPower(1000);
                material.setSpecularColor(Color.WHITE);
            } else {
                material.setSpecularPower(30);
                material.setSpecularColor(Color.LIGHTGRAY);
            }
        } else {
            // Fallback colors
            Color diffuseColor = switch(textureName) {
                case "Sun" -> Color.YELLOW;
                case "Mercury" -> Color.MEDIUMPURPLE;
                case "Venus" -> Color.ORANGE;
                case "Earth" -> Color.BLUE;
                case "Mars" -> Color.ORANGERED;
                case "Jupiter" -> Color.ORANGE;
                default -> Color.GRAY;
            };
            material.setDiffuseColor(diffuseColor);
            material.setSpecularColor(Color.WHITE);
            material.setSpecularPower(10);
        }
        // some properties of my other testing class
        planet.setMaterial(material);
    }
}

package edu.vanier.template.sim;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * Trail object attached to a Body
 * @author Le Tuan Huy Nguyen
 */
public class Trail {
    ArrayList<Sphere> trailParticles = new ArrayList<>();
        
    int frequency = 2;
    int internalClock = 0;
    int maxParticles = 100;

    boolean isActive = true;

    private Color colorTrail;
    public PhongMaterial phongMaterialTrail;
    
    public Trail(){
        colorTrail = Color.WHITE;
        phongMaterialTrail = new PhongMaterial(this.colorTrail);
    }
    
    /**
     * Add and remove trail particles accordingly
     * @param followBody the body that the trail is attached to
     */
    public void update(Body followBody) {
        if( !isActive || followBody == null) return;
        if (internalClock % frequency == 0){
            if (trailParticles.size() == maxParticles){
                Group parentNode = (Group) followBody.getParent();
                parentNode.getChildren().remove(trailParticles.get(0));
                trailParticles.remove(0);
            }
            Sphere particle = new Sphere(5);
            particle.setMaterial(this.phongMaterialTrail);
            
            trailParticles.add(particle);
            particle.setTranslateX(followBody.getTranslateX());
            particle.setTranslateY(followBody.getTranslateY());
            particle.setTranslateZ(followBody.getTranslateZ());

            Group parentNode = null;
            Node node = followBody.getParent();
            if(node instanceof  Group){
                parentNode = (Group) node;
            }
            if(parentNode != null) {
                parentNode.getChildren().add(particle);
            }else {
                internalClock += 1;
            }
        }

    }
    
    /**
     * Set the color of the trail
     * @param newColor the new color to set to
     */
    public void setTrailColor(Color newColor) {
        this.phongMaterialTrail.setDiffuseColor(newColor);
        this.phongMaterialTrail.setSpecularColor(colorTrail);
    }

    /**
     * This method set the activate state
     * @param activeState, the active state parameter
     */
    public void setActive(boolean activeState){
        this.isActive = activeState;
    }

    public ArrayList<Sphere> getTrailParticles() {
        return trailParticles;
    }
}

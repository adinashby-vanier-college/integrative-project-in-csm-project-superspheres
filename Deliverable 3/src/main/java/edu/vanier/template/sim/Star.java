package edu.vanier.template.sim;

import edu.vanier.template.math.Vector3D;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;

public class Star extends Body{
    PointLight pointLight = new PointLight(Color.WHITE);

    public Star(Vector3D position, Vector3D velocity, double mass, double radius, int divisions) {
        super(position, velocity, mass, radius, divisions);
    }

    public Star(Vector3D position, Vector3D velocity, double mass, double d) {
        super(position, velocity, mass, d);
    }
    
    public void update(double deltaTime){
        acceleration = force.scaleVector3D(1/mass);
        velocity.addToCurrentVector3D(acceleration.scaleVector3D(deltaTime));
        position.addToCurrentVector3D(velocity.scaleVector3D(deltaTime));
        
        setTranslateX(position.getX());
        setTranslateY(position.getY());
        setTranslateZ(position.getZ());
        pointLight.setTranslateX(position.getX());
        pointLight.setTranslateY(position.getY());
        pointLight.setTranslateZ(position.getZ());
    }
}

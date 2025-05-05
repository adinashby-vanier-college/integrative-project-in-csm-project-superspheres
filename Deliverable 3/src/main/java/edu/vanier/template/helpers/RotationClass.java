package edu.vanier.template.helpers;

import edu.vanier.template.sim.Body;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will help us handle rotations
 * @author Josue
 */
public class RotationClass {
    private Shape3D bodyToRotate;
    private  double rotationRate;
    private  static Map<Shape3D, RotationData> rotatingBodies = new HashMap<>();

    public  static  void updateAllRotations(double deltaTime){
        if (rotatingBodies.isEmpty()) return;
        rotatingBodies.forEach((body,data)->{
            data.rotate.setAngle(data.rotate.getAngle() + (data.rotationRate * deltaTime));
        });
    }

    public void addBody(Shape3D body, double rotationRate){
        Rotate rotate = new Rotate(0, Rotate.Y_AXIS);
        body.getTransforms().add(rotate);

        rotatingBodies.put(body,new RotationData(rotate, rotationRate));
    }
    private  static class RotationData{
        Rotate rotate;
        double rotationRate;
        RotationData(Rotate rotate, double rotationRate){
            this.rotate = rotate;
            this.rotationRate = rotationRate;
        }
    }
    public static void clearAllRotations(){
        rotatingBodies.clear();
    }
}

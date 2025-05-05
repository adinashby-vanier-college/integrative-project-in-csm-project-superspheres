package edu.vanier.template.sim;

import edu.vanier.template.math.Vector3D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Josue
 */
public class CameraControlsHandler {
    private final static Logger logger = LoggerFactory.getLogger(CameraControlsHandler.class);
    private PerspectiveCamera camera;

    //This is for rotations yaw is about the Y axis (left or right) and pitch is a rotation about the X axis (up and down)
    private double yaw = 0;
    private double pitch = 0;

    private double maxSpeed = 200;

    private double speed = 100;

    private double acceleration = 50;

    private double deceleration = 2.5;

    private boolean rightClickedHeld = false;

    private double lastMouseX, lastMouseY;

    private final Set<KeyCode> activeKeys = new HashSet<>();

    private boolean isMovementAllow = false;

    private Vector3D prevMovementVector = new Vector3D(0, 0, 0);
    private Vector3D vector3DMoveVector = new Vector3D(0, 0, 0);
    
    private boolean focused = false;
    
    private Body followBody;
    
    private Transform rotation;

    public CameraControlsHandler(PerspectiveCamera camera) {
        this.camera = camera;
    }

    public Vector3D getPrevMovementVector() {
        return prevMovementVector;
    }

    /**
     * This method will help handle the rotations along the axis ( X,Y)
     *
     * @param event
     */
    private void handleMouseLook(MouseEvent event) {
        if (!rightClickedHeld) {
            return;
        }

        //really important for drag detection and self explanatory
        double deltaX = event.getX() - lastMouseX;
        double deltaY = event.getY() - lastMouseY;

        // So here what is being done is based on the drag detections we will set a value for the rotations
        yaw += (deltaX / 2) * 0.3;
        pitch -= (deltaY / 2) * 0.3;

        pitch = Math.max(-49, Math.min(49, pitch));
      //  yaw = Math.max(-89, Math.min(89, yaw));

        updateCameraRotation();

        lastMouseX = event.getSceneX();
        lastMouseY = event.getSceneY();
    }

    /**
     * To update the camera's rotation this method use build in
     * properties(transforms)
     */
    public void updateCameraRotation() {
        // create concateneation is just adding multiple rotations transform in one transform
        //because if we add in one goe for some reason it did not work
        rotation = new Rotate(yaw, Rotate.Y_AXIS)
                .createConcatenation(new Rotate(pitch, Rotate.X_AXIS));

        camera.getTransforms().setAll(rotation);
    }

    public void updateMovement(double deltaTime) {
        double radYaw = Math.toRadians(yaw);
        double radPitch = Math.toRadians(pitch);

        // this step is crucial since forward is not always forward so we have to get a relative direction
        // just like in roblox where you cant get a look vector, to do that we use our yaw and pitch values
        /*
        Keep in mind javafx's axis are inverted !
         Understand did the trig:
         Math.sin(radYaw) : what it does is essentially take the x and z forward direction
         Math.pitch(radPitch):
         */

            vector3DMoveVector = new Vector3D(0, 0, 0);

            Vector3D vector3DForward = new Vector3D(
                    Math.sin(radYaw) * Math.cos(radPitch),
                    -Math.sin(radPitch),
                    Math.cos(radYaw) * Math.cos(radPitch)
            );

            Vector3D vector3DRight = new Vector3D(
                    Math.cos(radYaw),
                    0,
                    -Math.sin(radYaw)
            );
            
            
            
            if (activeKeys.contains(KeyCode.W)) {
                vector3DMoveVector.addToCurrentVector3D(vector3DForward);
            }
            if (activeKeys.contains(KeyCode.S)) {
                vector3DMoveVector.addToCurrentVector3D(vector3DForward.scaleVector3D(-1));                
            }
            if (activeKeys.contains(KeyCode.A)) {
                vector3DMoveVector.addToCurrentVector3D(vector3DRight.scaleVector3D(-1));
            }
            if (activeKeys.contains(KeyCode.D)) {
                vector3DMoveVector.addToCurrentVector3D(vector3DRight);
            }
            if (activeKeys.contains(KeyCode.Q)) {
                vector3DMoveVector.addToCurrentVector3D(new Vector3D(0, 1, 0));
            }
            if (activeKeys.contains(KeyCode.E)) {
                vector3DMoveVector.addToCurrentVector3D(new Vector3D(0, -1, 0));
            }
            if (activeKeys.contains(KeyCode.SPACE)) {
                vector3DMoveVector.addToCurrentVector3D(new Vector3D(0, -1, 0));
            }
            if (activeKeys.contains(KeyCode.SHIFT)) {
                vector3DMoveVector.addToCurrentVector3D(new Vector3D(0, 1, 0));
            }
            if (activeKeys.isEmpty()){
                vector3DMoveVector = new Vector3D(0, 0, 0);
            }
            
            vector3DMoveVector.normalizeVector3D();
            
            if(!activeKeys.isEmpty() && focused){
                focused = false;
                yaw = 270;
                pitch = 0;
            }
            
            Vector3D cameraMovements;
            if (!focused){
                if (!activeKeys.isEmpty()) {
                    if (speed > maxSpeed) {
                        speed = maxSpeed;
                    }
                    speed += acceleration * deltaTime;
                    cameraMovements = vector3DMoveVector
                            .scaleVector3D(speed)
                            .scaleVector3D(deltaTime);
                    prevMovementVector = vector3DMoveVector;
                } else {
                    speed -= speed * deceleration * deltaTime;
                    cameraMovements = prevMovementVector
                            .scaleVector3D(speed)
                            .scaleVector3D(deltaTime);

                    if (Math.abs(speed) <= 0.0001) {
                        speed = 0;
                    }
                }
                
                camera.setTranslateX(camera.getTranslateX() + cameraMovements.getX());
                camera.setTranslateY(camera.getTranslateY() + cameraMovements.getY());
                camera.setTranslateZ(camera.getTranslateZ() + cameraMovements.getZ());
            }
            else if (focused){
                rotation = new Rotate(270, Rotate.Y_AXIS);
                camera.getTransforms().setAll(rotation);

                camera.setTranslateX(followBody.getTranslateX() + followBody.getRadius() * 15);
                camera.setTranslateY(followBody.getTranslateY());
                camera.setTranslateZ(followBody.getTranslateZ());
                
            }

        }
    
    private int value;
    public void handleCamera(Scene scene) {
        if(scene == null) return;
        if (isMovementAllow && this.camera != null) {
            scene.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    rightClickedHeld = true;
                    lastMouseX = event.getSceneX();
                    lastMouseY = event.getSceneY();
                }
            });
            scene.setOnMouseReleased(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    rightClickedHeld = false;
                }
            });

            scene.setOnMouseDragged(this::handleMouseLook);
            scene.setOnKeyPressed(event -> activeKeys.add(event.getCode()));
            scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));
        }
    }

    public void setMovementAllow(boolean movementAllow) {
        isMovementAllow = movementAllow;
    }

    public Point3D getLookVector(){
        //Since the camera is in the scene we get transforms relative to the camera contained in the  (subscene) why ?
        // why is this important ? After some research its important because it  takes into account the absolute position
        // why not just get the camera transforms ? well there is no reason in our case it does not really matter but I'm just making sure if in the future we put our camera
        //to a group it does not interfere
        Transform transformCamera = camera.getLocalToSceneTransform();

        // this is going to be the default forward  without rotation , in javafx3D since the axis or inverted, I'm not sure but ... we will leave it as this
        Point3D localLook = new Point3D(0,0,1);

        // well this is the part that tricked me a lot but i think now i got it :
        // so basicly see it as the following analogy
        /*
        Person A is looking straight ahead and person B is also looking straight ahead
        but now you tell them where you are looking at they will both tell you im looking straight ahead,
        but if you ask them where you are looking based on the room they are in e.g: person A looking at the bed
        person B  looking at the right side of the bed
        the difference lies in on how person a and b are rotated in the room

        Same logic applies here we want  to check where a person is looking based on the camera where its looking straight ahead
        and fortunatly i dont have to do any calculations:

        the method deltaTransforms only take into account rotation apparently, im not sure tho
         */
        Point3D point3DWorldLook = transformCamera.deltaTransform(localLook);

        logger.info("Look vector " + point3DWorldLook.normalize());
        return  point3DWorldLook.normalize();
    }

    public void reset() {
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(-1000);

        yaw = 0;
        pitch = 0;
        updateCameraRotation();
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public Body getFollowBody() {
        return followBody;
    }

    public void setFollowBody(Body followBody) {
        this.followBody = followBody;
    }

    public Set<KeyCode> getActiveKeys() {
        return activeKeys;
    }
}

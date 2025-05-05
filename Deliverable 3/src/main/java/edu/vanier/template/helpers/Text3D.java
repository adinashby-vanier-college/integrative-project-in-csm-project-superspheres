package edu.vanier.template.helpers;

import edu.vanier.template.controllers.TemplateSelectionController;
import edu.vanier.template.math.Vector3D;
import edu.vanier.template.sim.Body;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  The Text3D class is a class that displays a 2D text in a 3D environment.
 * @author Joseph Josue Forestal
 */
public class Text3D extends Group {
    private final static Logger logger = LoggerFactory.getLogger(Text3D.class);

    private static  final double MIN_SCALE = 5, MAX_SCALE = 20, SCALE_RANGE = 50000.0;
    public String textTocheck;
    private Rotate rotateY = new Rotate(0,Rotate.Y_AXIS);
    private Body parentBody;

    private PerspectiveCamera perspectiveCamera;
    private   Text meshText ;


    private double x,  y,  z,  size = 12;
    Vector3D vector3DPosition = new Vector3D(x,y,z);

    /**
     * The argument construct of Text 3D
     * @param text ,the text you want to be displayed
     * @param color  ,the color of the text
     * @param parentBody , the body that this text will be assigned to
     * @param camera ,the camera of the 3D world
     */
    public Text3D(String text, Color color,Body parentBody, PerspectiveCamera camera) {
        this.textTocheck = text; // place holder because im going to use it in build visilazation

        this.parentBody = parentBody;
        this.perspectiveCamera = camera;

        meshText = new Text(textTocheck);
        meshText.setFill(color);
        meshText.setStyle("-fx-font-size: " + size + "; -fx-font-weight: bold;");

        this.getChildren().add(meshText);
        this.getTransforms().add(rotateY);

}

    /**
     * this method updates the position of the text in the 3D space relative to the sphere
     */
  public void updatePosition(){
        this.setTranslateX(parentBody.getTranslateX());
        this.setTranslateY(parentBody.getTranslateY() - (parentBody.getRadius() * 2));
        this.setTranslateZ(parentBody.getTranslateZ());
        this.rotateToTargetPosition(new Vector3D(perspectiveCamera.getTranslateX(), perspectiveCamera.getTranslateY(),perspectiveCamera.getTranslateZ()));
        updateScaleBasedOnDistance();

  }

    /**
     * This method set the size of the text
     * @param size ,the size at which the text must be set
     */
    public void setSize(double size) {
        this.size = size;
        meshText.setStyle("-fx-font-size: " + this.size + "; -fx-font-weight: bold;");

    }

    /**
     * This method rotate the tet towards the camera.
     * @param targetPosition ,the position of the camera in our 3D space
     */
    public void rotateToTargetPosition(Vector3D targetPosition){
        double angleOfRotation = Math.atan2(targetPosition.getZ() - this.getTranslateZ(), targetPosition.getX() - this.getTranslateX());
        this.rotateY.setAngle(-Math.toDegrees(angleOfRotation )  -90);
        //this.rotateY.setPivotY(10);
   }

    /**
     * A tentative method that update the scale or the size based on the distance the camera
     */
   private void updateScaleBasedOnDistance(){
       Vector3D vector3DDistance = new Vector3D(perspectiveCamera.getTranslateX() - this.getTranslateX(), perspectiveCamera.getTranslateY() - this.getTranslateY(), perspectiveCamera.getTranslateZ() - this.getTranslateZ());
       double scale = MIN_SCALE + (MAX_SCALE - MIN_SCALE) * Math.min(1,Math.log1p(vector3DDistance.getMagnitude())/Math.log1p(SCALE_RANGE));

       this.setScaleX(scale);
       this.setScaleY(scale);
       //this.setScaleZ(scale);
   }

    /**
     * Set  the current text and directly updates it ,in the mesh text.
     * @param textTocheck ,the new text
     */
    public void setTextTocheck(String textTocheck) {
        if(!textTocheck.isEmpty() && !textTocheck.isBlank()){
            this.textTocheck = textTocheck;
            this.meshText.setText(this.textTocheck);
            logger.info("Text was succesfuly set to " +  textTocheck);
        }
    }

    /**
     * This method get the current text.
     * @return the current text.
     */
    public String getTextTocheck() {
        return this.textTocheck;
    }
}

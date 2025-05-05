package edu.vanier.template.helpers;

import edu.vanier.template.sim.Body;
import edu.vanier.template.sim.CameraControlsHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;

/**
 * This class is a tentative method of Roblox's raycasting, which find the closest planet that the camera is looking too
 * @author Josue
 */
public class RayCaster {
    private Group groupRoot;
    private CameraControlsHandler cameraControlsHandler;
    private PerspectiveCamera perspectiveCamera;

    /**
     * Constructor of the ray caster class
     * @param groupRoot ,the group in which all the bodies or in
     * @param cameraControlsHandler ,the class that handle the camera movements
     */
    public  RayCaster(Group groupRoot, CameraControlsHandler cameraControlsHandler){
        this.groupRoot = groupRoot;
        this.cameraControlsHandler = cameraControlsHandler;
        perspectiveCamera = this.cameraControlsHandler.getCamera();
    }

    /**
     * This subclass is the result of the closet planet being hit by the ray cast.
     * @author Josue
     */
    public  static  class RayCastResult{
        public Body hitBody;
        public Point3D hitPoint;
        public  double distance;

        /**
         * Constructor  of RayCastResult
         * @param hitBody, the body that is being hit
         * @param hitPoint, the hitPoint which is a point3D in the scene
         * @param distance, and the double distance
         */
        public  RayCastResult(Body hitBody, Point3D hitPoint, double distance){
            this.hitBody = hitBody;
            this.hitPoint = hitPoint;
            this.distance = distance;
        }
    }

    /**
     * This method cast a Ray
     * @param maxDistance, the max distance at which this ray Cast works
     * @return a RayCastResult object
     */
 public  RayCastResult castRay(double maxDistance){
        Point3D origin = getCameraWorldPosition();
        Point3D direction = cameraControlsHandler.getLookVector();

       return findClosestIntersection(origin, direction, maxDistance);
 }

    /**
     * Get the camera world postion (the absolute position in the scene)
     * @return A point3D
     */
 public  Point3D getCameraWorldPosition(){
        return this.perspectiveCamera.localToScene(Point3D.ZERO);
 }

    /**
     * This method find the closest point of Intersection
     * @param origin ,the orginal position(Point3D)
     * @param direction ,the direction at which this ray is being cast(Point3D)
     * @param maxDistance , the max distance of the ray (double)
     * @return a RayCastResult Object
     */
 private  RayCastResult findClosestIntersection(Point3D origin, Point3D direction, double maxDistance){
        RayCastResult closetHit = null;
        double closestDistance = maxDistance;

        for(Node node: groupRoot.getChildren()){
            if(node instanceof  Body body){

                Point3D center = body.localToScene(Point3D.ZERO);

                double radius = body.getRadius();
                if(radius <= 0 ) continue;

                Point3D toCenter  = center.subtract(origin);
                double projection = toCenter.dotProduct(direction);

                // if the dot product is less than zero we know its behind so we don't care

                if(projection < 0) continue;
                double distanceSquared = toCenter.dotProduct(toCenter) - projection * projection;
                double radiusSquared = radius * radius;

                if(distanceSquared <= radiusSquared){
                    double distance = projection - Math.sqrt(radiusSquared - distanceSquared);

                    if(distance > 0 && distance < closestDistance){
                        closestDistance = distance;
                        Point3D hitPoint = origin.add(direction.multiply(distance));
                        closetHit  = new RayCastResult(body, hitPoint, distance);
                    }


                }
            }
        }
        return closetHit;
 }
}

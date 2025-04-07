package edu.vanier.template.helpers;

import edu.vanier.template.sim.Body;
import edu.vanier.template.sim.CameraControlsHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;

public class RayCaster {
    private Group groupRoot;
    private CameraControlsHandler cameraControlsHandler;
    private PerspectiveCamera perspectiveCamera;

    public  RayCaster(Group groupRoot, CameraControlsHandler cameraControlsHandler){
        this.groupRoot = groupRoot;
        this.cameraControlsHandler = cameraControlsHandler;
        perspectiveCamera = this.cameraControlsHandler.getCamera();
    }

    public  static  class RayCastResult{
        public Body hitBody;
        public Point3D hitPoint;
        public  double distance;

        public  RayCastResult(Body hitBody, Point3D hitPoint, double distance){
            this.hitBody = hitBody;
            this.hitPoint = hitPoint;
            this.distance = distance;
        }
    }


 public  RayCastResult castRay(double maxDistance){
        Point3D origin = getCameraWorldPosition();
        Point3D direction = cameraControlsHandler.getLookVector();

       return findClosestIntersection(origin, direction, maxDistance);
 }

 public  Point3D getCameraWorldPosition(){
        return this.perspectiveCamera.localToScene(Point3D.ZERO);
 }

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

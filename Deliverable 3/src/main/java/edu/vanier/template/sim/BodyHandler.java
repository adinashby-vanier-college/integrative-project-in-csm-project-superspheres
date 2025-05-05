/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.template.sim;

import edu.vanier.template.controllers.SimulationMainPageController;
import edu.vanier.template.controllers.TemplateSelectionController;
import edu.vanier.template.math.Physics;
import edu.vanier.template.math.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * A group of Bodys that can calculate forces applied on one another each frame to
 * determine movement
 * @author Le Tuan Huy Nguyen
 */
public class BodyHandler {
    private final static Logger logger = LoggerFactory.getLogger(BodyHandler.class);
    private ArrayList<Body> bodies;

    public BodyHandler() {
        bodies = new ArrayList<>();
    }

    /**
     * Add forces on bodies depending on other bodies in the BodyHandler
     * @param deltaTime 
     */
    public void update(double deltaTime){

        for(Body target: bodies){
            target.setForce(new Vector3D(0, 0, 0));

            for(Body current: bodies){
                if(target == current){continue;}
                Vector3D distanceVector = new Vector3D(
                        current.getPosition().getX() - target.getPosition().getX(),
                        current.getPosition().getY() - target.getPosition().getY(),
                        current.getPosition().getZ() - target.getPosition().getZ()
                );
               if(distanceVector.getMagnitude() > current.getRadius() + target.getRadius())target.addForce(Physics.calculateGForce(target, current));
            }
        }

        for(Body target: bodies){
            target.update(deltaTime);
        }

    }

    /**
     * Add a new body
     * @param body body to be added
     */
    public void add(Body body){
        bodies.add(body);
        if(body.nameLabel!= null)
            SimulationMainPageController.getLastInstance().getGroupRootNode().getChildren().add(body.nameLabel);
    }
    
    /**
     * Add many new bodies
     * @param bodies bodies to be added
     */
    public void addAll(Body... bodies){
        for(Body body : bodies){add(body);}
    }
    public ArrayList<Body> getBodies() {
        return bodies;
    }
}

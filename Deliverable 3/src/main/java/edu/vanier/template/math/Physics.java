/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.template.math;

import edu.vanier.template.sim.Body;

/**
 * Handle Gravitational physics applied onto the body
 * @author Le Tuan Huy Nguyen
 */
public class Physics {
    
    public static final double G = 9.8;
    
    /**
     * Calculates the force applied onto the target body by the current body.
     * @param target The body of which the force is applied and returned
     * @param current The body of which is applying the gravitational force
     * @return The force applied onto the target body
     */
    public static Vector3D calculateGForce(Body target, Body current){
        
        double d = target.getPosition().getDistance(current.getPosition());
        Vector3D direction = target.getPosition().getDirection(current.getPosition());
        double massTarget = target.getMass();
        double massCurrent = current.getMass();
        
        double scalarForce = G * massTarget * massCurrent / (d * d);
        Vector3D force = direction.normalizeVector3D().scaleVector3D(scalarForce);
        return force;
    }
    
}

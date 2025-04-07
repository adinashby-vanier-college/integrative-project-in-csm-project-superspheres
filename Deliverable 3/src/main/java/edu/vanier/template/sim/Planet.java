package edu.vanier.template.sim;

import edu.vanier.template.math.Vector3D;

public class Planet extends Body{

    public Planet(Vector3D position, double mass, double radius) {
        super(position, mass, radius);
    }
    
    public Planet(Vector3D position, Vector3D velocity, double mass, double radius, int divisions) {
        super(position, velocity, mass, radius, divisions);
    }

    public Planet(Vector3D position, Vector3D velocity, double mass, double d) {
        super(position, velocity, mass, d);
    }
}

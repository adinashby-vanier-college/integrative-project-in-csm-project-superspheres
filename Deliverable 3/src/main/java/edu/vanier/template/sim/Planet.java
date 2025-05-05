package edu.vanier.template.sim;

import edu.vanier.template.math.Vector3D;

/**
 * Planet extension of Body
 * @author Le Tuan Huy Nguyen
 */
public class Planet extends Body{

    public Planet(Vector3D position, double mass, double radius) {
        super(position, mass, radius);
    }
    
    public Planet(Vector3D position, Vector3D velocity, double mass, double radius, int divisions) {
        super(position, velocity, mass, radius, divisions);
    }

    public Planet(Vector3D position, Vector3D velocity, double mass, double radius) {
        super(position, velocity, mass, radius);
    }



    public String toSave() {
        String name = getName();
        return "Body{" +
                "position=" + position +
                ", velocity=" + velocity +
                ", acceleration=" + acceleration +
                ", force=" + force +
                ", mass=" + mass +
                ", name="+ name +
                ", radius=" + this.getRadius()+
                '}';}
}

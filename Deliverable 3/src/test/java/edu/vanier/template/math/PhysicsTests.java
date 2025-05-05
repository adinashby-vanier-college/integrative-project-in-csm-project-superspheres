package edu.vanier.template.math;

import edu.vanier.template.sim.Body;
import edu.vanier.template.sim.Planet;
import edu.vanier.template.sim.Star;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author letua
 */
public class PhysicsTests {
    
    
    // Tolerance
    private static final double DELTA = 0.0001;

    
    @Test
    void testCalculateGForce() {
        
        Body body1 = new Planet(new Vector3D(0, 0, 0), 1, 1);
        Body body2 = new Planet(new Vector3D(5, 0, 0), 1, 1);
        
        Vector3D force = Physics.calculateGForce(body1, body2);
        // Needs to be G * m1 * m2 / d^2 = 9.8 * 1 * 1 / 5^2 = 0.392
        
        assertEquals(force.getX(), 0.392, DELTA);
        
        body1 = new Planet(new Vector3D(0, 0, 0), 1, 1);
        body2 = new Planet(new Vector3D(3, 4, 0), 1, 1);
        
        force = Physics.calculateGForce(body1, body2);
        // Needs to be G * m1 * m2 / d^2 = 9.8 * 1 * 1 / 5^2 = 0.392
        double angle = Math.atan2(4, 3);
        // Now angled x = 0.392 * cos(angle), y = 0.392 * sin(angle)
        
        assertEquals(force.getX(), 0.392 * Math.cos(angle), DELTA);
        assertEquals(force.getY(), 0.392 * Math.sin(angle), DELTA);
    }
}

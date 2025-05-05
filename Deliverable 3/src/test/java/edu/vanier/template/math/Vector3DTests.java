package edu.vanier.template.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector3DTests {

    // Tolerance
    private static final double DELTA = 0.0001;

    @Test
    void testAddVector3D() {
        Vector3D v1 = new Vector3D(1, 2, 3);
        Vector3D v2 = new Vector3D(4, 5, 6);
        Vector3D result = v1.addVector3D(v2);

        assertEquals(5, result.getX(), DELTA);
        assertEquals(7, result.getY(), DELTA);
        assertEquals(9, result.getZ(), DELTA);

        // Check that the operations weren't inplace operations
        assertEquals(1, v1.getX(), DELTA);
        assertEquals(2, v1.getY(), DELTA);
        assertEquals(3, v1.getZ(), DELTA);
        assertEquals(4, v2.getX(), DELTA);
        assertEquals(5, v2.getY(), DELTA);
        assertEquals(6, v2.getZ(), DELTA);
    }
    
    @Test
    void testAddToCurrentVector3D() {
        Vector3D v1 = new Vector3D(1, 2, 3);
        Vector3D v2 = new Vector3D(4, 5, 6);
        
        v1.addToCurrentVector3D(v2);

        assertEquals(5, v1.getX(), DELTA);
        assertEquals(7, v1.getY(), DELTA);
        assertEquals(9, v1.getZ(), DELTA);
        
        // Check that the other vector was not modified
        assertEquals(4, v2.getX(), DELTA);
        assertEquals(5, v2.getY(), DELTA);
        assertEquals(6, v2.getZ(), DELTA);
    }

    @Test
    void testScaleVector3D_scalesComponentsCorrectly() {
        Vector3D vector = new Vector3D(1, 2, 3);
        double scale = 2.5;
        Vector3D result = vector.scaleVector3D(scale);

        assertEquals(2.5, result.getX(), DELTA);
        assertEquals(5, result.getY(), DELTA);
        assertEquals(7.5, result.getZ(), DELTA);

        // Check that the original vector was not modified
        assertEquals(1, vector.getX(), DELTA);
        assertEquals(2, vector.getY(), DELTA);
        assertEquals(3, vector.getZ(), DELTA);
    }

    @Test
    void testGetMagnitude() {
        Vector3D v1 = new Vector3D(3, 4, 0); 
        assertEquals(5, v1.getMagnitude(), DELTA);

        Vector3D v2 = new Vector3D(1, 1, 1);
        assertEquals(Math.sqrt(3), v2.getMagnitude(), DELTA);
        
        Vector3D v0 = new Vector3D(0, 0, 0);
        assertEquals(0, v0.getMagnitude(), DELTA);
    }
    
    @Test
    void testNormalizeVector3D() {
        Vector3D v1 = new Vector3D(3, 4, 0);
        Vector3D normalizedV1 = v1.normalizeVector3D();

        assertEquals(0.6, normalizedV1.getX(), DELTA); // 3/5
        assertEquals(0.8, normalizedV1.getY(), DELTA); // 4/5
        assertEquals(0.0, normalizedV1.getZ(), DELTA); // 0/5
        
        // Check that the magnitude of the result is 1
        assertEquals(1, normalizedV1.getMagnitude(), DELTA);
        
        // Ensure it returned the modified original instance
        assertSame(v1, normalizedV1);
        
        Vector3D v0 = new Vector3D(0, 0, 0); // Magnitude 0
        Vector3D normalizedV0 = v0.normalizeVector3D(); // This modifies v0

        assertEquals(0, normalizedV0.getX(), DELTA);
        assertEquals(0, normalizedV0.getY(), DELTA);
        assertEquals(0, normalizedV0.getZ(), DELTA);
        
        // Magnitude should still be 0
        assertEquals(0, normalizedV0.getMagnitude(), DELTA);
        
        // Ensure it returned the modified original instance
        assertSame(v0, normalizedV0);
    }
    
    @Test
    void testGetDistance() {
        Vector3D p1 = new Vector3D(1, 2, 3);
        Vector3D p2 = new Vector3D(4, 6, 15); 
        // Difference vector is (3, 4, 12)
        // Magnitude of difference = sqrt(3^2 + 4^2 + 12^2) = sqrt(9+16+144) = sqrt(169) = 13

        assertEquals(13, p1.getDistance(p2), DELTA);

        // Distance from a point to itself should be 0
        assertEquals(0, p1.getDistance(p1), DELTA);
    }
}
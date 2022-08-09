package de.hsos.roomplanner.util;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents a position and orientation on a 2D layered canvas.
 * 
 * @author Christoph Freimuth
 */
@Embeddable
public class Position {

    private float x;
    private float y;
    private float z;
    private float rotation;

    @JsonCreator
    public Position(float x, float y, float z, float rotation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
    }

    public Position(float x, float y) { this(x, y, 0, 0); }

    public Position() { this(0, 0, 0, 0); }

    public float getX() { return x; }

    public void setX(float x) { this.x = x; }

    public float getY() { return y; }

    public void setY(float y) { this.y = y; }

    public float getZ() { return z; }

    public void setZ(float z) { this.z = z; }

    public float getRotation() { return rotation; }

    public void setRotation(float rotation) { this.rotation = rotation; }

}

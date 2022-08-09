package de.hsos.roomplanner.util;

import javax.persistence.Embeddable;
import javax.validation.constraints.Positive;

/**
 * @author Christoph Freimuth
 */
@Embeddable
public class Dimension {

    @Positive
    private float width;
    @Positive
    private float height;

    public Dimension(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Dimension() {}

    public float getWidth() { return width; }

    public void setWidth(float width) { this.width = width; }

    public float getHeight() { return height; }

    public void setHeight(float height) { this.height = height; }

    @Override
    public String toString() { return "Dimension [height=" + height + ", width=" + width + "]"; }

}

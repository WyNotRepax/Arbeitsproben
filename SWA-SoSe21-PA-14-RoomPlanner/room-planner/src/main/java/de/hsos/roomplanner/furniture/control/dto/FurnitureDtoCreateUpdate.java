package de.hsos.roomplanner.furniture.control.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import de.hsos.roomplanner.util.Dimension;
import de.hsos.roomplanner.util.color.Color;

/**
 * @author Benno Steinkamp
 */
public class FurnitureDtoCreateUpdate {

    @NotBlank
    private String name;

    @Valid
    private Dimension dimensions;

    @Valid
    private Color color;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Dimension getDimensions() { return dimensions; }

    public void setDimensions(Dimension dimensions) { this.dimensions = dimensions; }

    public Color getColor() { return color; }

    public void setColor(Color color) { this.color = color; }

}

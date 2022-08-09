package de.hsos.roomplanner.plan.control.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import de.hsos.roomplanner.util.Dimension;
import io.smallrye.common.constraint.NotNull;

/**
 * @author Christoph Freimuth
 */
public class PlanDtoCreateUpdate {

    @NotBlank
    private String name;

    @NotNull
    @Valid
    private Dimension dimensions;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Dimension getDimensions() { return dimensions; }

    public void setDimension(Dimension dimensions) { this.dimensions = dimensions; }

}

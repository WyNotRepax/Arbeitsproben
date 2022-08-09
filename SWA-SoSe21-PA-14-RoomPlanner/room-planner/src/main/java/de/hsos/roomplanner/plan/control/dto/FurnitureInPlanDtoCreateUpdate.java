package de.hsos.roomplanner.plan.control.dto;

import javax.validation.Valid;

import de.hsos.roomplanner.util.Position;
import io.smallrye.common.constraint.NotNull;

/**
 * @author Christoph Freimuth
 */
public class FurnitureInPlanDtoCreateUpdate {

    private long furnitureId;

    @NotNull
    @Valid
    private Position position;

    public Position getPosition() { return position; }

    public void setPosition(Position position) { this.position = position; }

    public long getFurnitureId() { return furnitureId; }

    public void setFurnitureId(long furnitureId) { this.furnitureId = furnitureId; }

}

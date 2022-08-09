package de.hsos.roomplanner.plan.control.dto;

/**
 * @author Christoph Freimuth
 */
public class FurnitureInPlanDto extends FurnitureInPlanDtoCreateUpdate {

    private long id;

    public FurnitureInPlanDto() { super(); }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

}

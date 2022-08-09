package de.hsos.roomplanner.plan.control.dto;

import java.util.List;

import javax.validation.Valid;

import io.smallrye.common.constraint.NotNull;

/**
 * @author Christoph Freimuth
 */
public class PlanDtoDetail extends PlanDtoListing implements PlanDtoHasFurniture {

    @NotNull
    @Valid
    private List<FurnitureInPlanDto> furnitureInPlan;

    public PlanDtoDetail() { super(); }

    @Override
    public List<FurnitureInPlanDto> getFurniture() { return furnitureInPlan; }

    @Override
    public void setFurniture(List<FurnitureInPlanDto> furnitureInPlan) { this.furnitureInPlan = furnitureInPlan; }

}

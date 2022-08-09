package de.hsos.roomplanner.plan.control.dto;

import java.util.List;

/**
 * @author Benno Steinkamp
 */
public interface PlanDtoHasFurniture {

    List<FurnitureInPlanDto> getFurniture();

    void setFurniture(List<FurnitureInPlanDto> furniture);

}

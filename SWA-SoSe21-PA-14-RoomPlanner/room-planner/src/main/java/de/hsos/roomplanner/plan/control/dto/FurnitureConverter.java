package de.hsos.roomplanner.plan.control.dto;

import javax.inject.Singleton;

import de.hsos.roomplanner.plan.entity.FurnitureInPlan;

/**
 * @author Benno Steinkamp
 */
@Singleton
public class FurnitureConverter {

    /**
     * Converts a furnitureInPlan entity to the corresponding Dto.
     * 
     * @param furnitureInPlan - furnitureInPlan entity to be converted.
     * @return - the corresponding FurnitureInPlanDto.
     */
    public FurnitureInPlanDto dtoFrom(FurnitureInPlan furnitureInPlan) {
        FurnitureInPlanDto furnitureDto = new FurnitureInPlanDto();
        furnitureDto.setId(furnitureInPlan.getId());
        furnitureDto.setFurnitureId(furnitureInPlan.getFurniture().getId());
        furnitureDto.setPosition(furnitureInPlan.getPosition());
        return furnitureDto;
    }

}

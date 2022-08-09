package de.hsos.roomplanner.furniture.control.dto;

import javax.inject.Singleton;

import de.hsos.roomplanner.furniture.entity.Furniture;

/**
 * @author Benno Steinkamp
 */
@Singleton
public class FurnitureConverter {

    /**
     * Converts a furniture entity to the corresponding Dto for detail request.
     * 
     * @param furniture - the furniture entity to be converted.
     * @return - the corresponding FurnitureDtoDetail
     */
    public FurnitureDto createDtoFrom(Furniture furniture) {
        FurnitureDto furnitureDto = new FurnitureDto();
        furnitureDto.setId(furniture.getId());
        furnitureDto.setName(furniture.getName());
        furnitureDto.setColor(furniture.getColor());
        furnitureDto.setDimensions(furniture.getDimension());
        return furnitureDto;
    }

}

package de.hsos.roomplanner.furniture.control;

import de.hsos.roomplanner.furniture.control.dto.FurnitureDtoCreateUpdate;

import javax.validation.Valid;

import de.hsos.roomplanner.furniture.control.dto.FurnitureDto;
import de.hsos.roomplanner.util.Page;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;
import de.hsos.roomplanner.util.exception.UserNotFoundException;

/**
 * @author Christoph Freimuth
 */
public interface FurnitureServiceRest {

    public Page<FurnitureDto> findFurnitures(String username, String filterName, int pageSize, int pageIndex)
            throws UserNotFoundException;

    public FurnitureDto findFurniture(String username, long id)
            throws EntityDoesNotExistException, UserNotFoundException;

    public FurnitureDto createFurniture(String username, @Valid FurnitureDtoCreateUpdate furniture)
            throws UserNotFoundException;

    public FurnitureDto updateFurniture(String username, long id, @Valid FurnitureDtoCreateUpdate furniture)
            throws EntityDoesNotExistException, UserNotFoundException;

    public void deleteFurniture(String username, long id) throws EntityDoesNotExistException, UserNotFoundException;

}

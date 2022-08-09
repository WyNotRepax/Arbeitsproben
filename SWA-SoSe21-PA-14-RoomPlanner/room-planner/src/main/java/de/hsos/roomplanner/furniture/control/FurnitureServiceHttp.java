package de.hsos.roomplanner.furniture.control;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.hsos.roomplanner.furniture.control.dto.FurnitureDto;
import de.hsos.roomplanner.util.Dimension;
import de.hsos.roomplanner.util.Page;
import de.hsos.roomplanner.util.color.Color;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;
import de.hsos.roomplanner.util.exception.UserNotFoundException;

/**
 * @author Christoph Freimuth
 */
public interface FurnitureServiceHttp {

        public Page<FurnitureDto> findFurnitures(String username, String filterName, int pageSize, int pageIndex) throws UserNotFoundException;

        public FurnitureDto findFurniture(String username, long id) throws EntityDoesNotExistException, UserNotFoundException;

        public FurnitureDto createFurniture(
                        String username,
                        @NotBlank String name,
                        @Valid @NotNull Dimension dimension,
                        @Valid @NotNull Color color
        ) throws UserNotFoundException;

        public FurnitureDto updateFurniture(
                        String username,
                        long id,
                        @NotBlank String name,
                        @Valid @NotNull Dimension dimension,
                        @Valid @NotNull Color color
        ) throws EntityDoesNotExistException, UserNotFoundException;

        public void deleteFurniture(String username, long id) throws EntityDoesNotExistException, UserNotFoundException;

}

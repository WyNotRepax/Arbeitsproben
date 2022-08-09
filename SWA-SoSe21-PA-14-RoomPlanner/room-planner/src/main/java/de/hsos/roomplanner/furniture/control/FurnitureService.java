package de.hsos.roomplanner.furniture.control;

import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.hsos.roomplanner.furniture.control.dto.FurnitureConverter;
import de.hsos.roomplanner.furniture.control.dto.FurnitureDto;
import de.hsos.roomplanner.furniture.control.dto.FurnitureDtoCreateUpdate;
import de.hsos.roomplanner.furniture.entity.Furniture;
import de.hsos.roomplanner.furniture.entity.ImmutableUserFurniture;
import de.hsos.roomplanner.furniture.gateway.FurnitureRepository;
import de.hsos.roomplanner.furniture.gateway.UserRepository;
import de.hsos.roomplanner.util.Dimension;
import de.hsos.roomplanner.util.Page;
import de.hsos.roomplanner.util.color.Color;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;
import de.hsos.roomplanner.util.exception.UserNotFoundException;

/**
 * @author Benno Steinkamp
 * @author Christoph Freimuth
 */
@RequestScoped
public class FurnitureService implements FurnitureServiceRest, FurnitureServiceHttp {

    @Inject
    FurnitureConverter furnitureConverter;

    @Inject
    FurnitureRepository furnitureRepository;

    @Inject
    UserRepository userRepository;

    /**
     * Method to create a new furniture.
     * 
     * @param name      - the name of the furniture to be created.
     * @param dimension - the dimension of the furniture to be created.
     * @param color     - the color of the furniture to be created.
     */
    @Override
    @Transactional
    public FurnitureDto createFurniture(
            String username,
            @NotBlank String name,
            @Valid @NotNull Dimension dimension,
            @Valid @NotNull Color color
    ) throws UserNotFoundException {
        ImmutableUserFurniture user = this.findUser(username);
        Furniture newFurniture = new Furniture(name, dimension, color);
        newFurniture.setUser(user);
        furnitureRepository.persist(newFurniture);
        return this.furnitureConverter.createDtoFrom(newFurniture);
    }

    /**
     * Method to update an existing furniture.
     * 
     * @param username  - The name of the user for which a Furniture Object should
     *                  be created
     * @param id        - the ID of the furniture to be updated.
     * @param name      - the new name of the furniture to be updated.
     * @param dimension - the new dimension of the furniture to be updated.
     * @param color     - the new color of the furniture to be updated.
     */
    @Override
    @Transactional
    public FurnitureDto updateFurniture(
            String username,
            long id,
            @NotBlank String name,
            @Valid @NotNull Dimension dimension,
            @Valid @NotNull Color color
    ) throws EntityDoesNotExistException, UserNotFoundException {
        ImmutableUserFurniture user = this.findUser(username);
        Furniture existingFurniture = furnitureRepository.find(user, id);
        existingFurniture.setColor(color);
        existingFurniture.setDimension(dimension);
        existingFurniture.setName(name);
        return this.furnitureConverter.createDtoFrom(existingFurniture);

    }

    /**
     * Method to find a list of furnitures.
     * 
     * @param filterName - the name to filter by.
     * @param pageSize   - the size of the page to be returned.
     * @param pageIndex  - the 0 starting index of the page of results to be
     *                   returned.
     * @return page of results.
     */
    @Override
    @Transactional
    public Page<FurnitureDto> findFurnitures(String username, String filterName, int pageSize, int pageIndex)
            throws UserNotFoundException {
        ImmutableUserFurniture user = this.findUser(username);
        Page<Furniture> furniture = furnitureRepository.findAll(user, filterName, pageSize, pageIndex);
        Page<FurnitureDto> furnitureDtoListing = new Page<FurnitureDto>(
                furniture.getTotalCount(),
                furniture.getData().stream().map(furnitureConverter::createDtoFrom).collect(Collectors.toList())
        );
        return furnitureDtoListing;
    }

    /**
     * Method to find a specific furniture.
     * 
     * @param id - the ID of the furniture to find.
     * @return the furniture found.
     */
    @Override
    @Transactional
    public FurnitureDto findFurniture(String username, long id)
            throws EntityDoesNotExistException, UserNotFoundException {
        ImmutableUserFurniture user = this.findUser(username);
        return furnitureConverter.createDtoFrom(furnitureRepository.find(user, id));
    }

    /**
     * Method to create a new furniture.
     * 
     * @param furniture - the values of the furniture to be created.
     * @return corresponding FurnitureDtoDetail.
     */
    @Override
    @Transactional
    public FurnitureDto createFurniture(String username, @Valid FurnitureDtoCreateUpdate furniture)
            throws UserNotFoundException {
        return this.createFurniture(username, furniture.getName(), furniture.getDimensions(), furniture.getColor());
    }

    /**
     * Method to update an existing furniture.
     * 
     * @param id        - the ID of the furniture to be updated.
     * @param furniture - the values of the furniture to be updated.
     * @return corresponding FurnitureDtoDetail.
     */
    @Override
    @Transactional
    public FurnitureDto updateFurniture(String username, long id, @Valid FurnitureDtoCreateUpdate furniture)
            throws EntityDoesNotExistException, UserNotFoundException {
        return this.updateFurniture(username, id, furniture.getName(), furniture.getDimensions(), furniture.getColor());
    }

    /**
     * Method to delete an existing furniture.
     * 
     * @param id - the ID of the furniture to be deleted.
     * @return true if furniture got deleted, false if not.
     */
    @Override
    @Transactional
    public void deleteFurniture(String username, long id) throws EntityDoesNotExistException, UserNotFoundException {
        ImmutableUserFurniture user = this.findUser(username);
        furnitureRepository.delete(user, id);
    }

    /**
     * @param username
     * @return ImmutableUserFurniture
     * @throws UserNotFoundException
     */
    private ImmutableUserFurniture findUser(String username) throws UserNotFoundException {
        try {
            return this.userRepository.findUser(username);
        } catch (EntityDoesNotExistException ex) {
            throw new UserNotFoundException(username);
        }
    }

}

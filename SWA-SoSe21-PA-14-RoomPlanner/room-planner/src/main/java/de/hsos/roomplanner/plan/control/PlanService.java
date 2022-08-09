package de.hsos.roomplanner.plan.control;

import java.time.LocalDate;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;

import de.hsos.roomplanner.plan.control.dto.FurnitureConverter;
import de.hsos.roomplanner.plan.control.dto.FurnitureInPlanDto;
import de.hsos.roomplanner.plan.control.dto.FurnitureInPlanDtoCreateUpdate;
import de.hsos.roomplanner.plan.control.dto.PlanConverter;
import de.hsos.roomplanner.plan.control.dto.PlanDtoCreateUpdate;
import de.hsos.roomplanner.plan.control.dto.PlanDtoDetail;
import de.hsos.roomplanner.plan.control.dto.PlanDtoListing;
import de.hsos.roomplanner.plan.entity.FurnitureInPlan;
import de.hsos.roomplanner.plan.entity.ImmutableFurniture;
import de.hsos.roomplanner.plan.entity.ImmutableUserPlan;
import de.hsos.roomplanner.plan.entity.Plan;
import de.hsos.roomplanner.plan.gateway.FurnitureRepository;
import de.hsos.roomplanner.plan.gateway.PlanRepository;
import de.hsos.roomplanner.plan.gateway.UserRepository;
import de.hsos.roomplanner.util.Dimension;
import de.hsos.roomplanner.util.Page;
import de.hsos.roomplanner.util.date.DateUtil;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;
import de.hsos.roomplanner.util.exception.UserNotFoundException;

/**
 * @author Benno Steinkamp
 * @author Christoph Freimuth
 */
@RequestScoped
public class PlanService implements PlanServiceHttp, PlanServiceRest {

    @Inject
    PlanRepository planRepository;

    @Inject
    FurnitureRepository furnitureRepository;

    @Inject
    PlanConverter planConverter;

    @Inject
    FurnitureConverter furnitureConverter;

    @Inject
    UserRepository userRepository;

    /**
     * Creates a plan for the user
     * 
     * @param username - The username of the user for which a plan should be created
     * @param plan     - The plan that should be created
     * @return PlanDtoDetail - the PlanDetailDto representation of the Plan that was
     *         created
     * @throws UserNotFoundException - If no user with the username could be found
     * 
     * @see {@link #createPlan(String, String, Dimension)}
     */
    @Override
    @Transactional
    public PlanDtoDetail createPlan(String username, @Valid PlanDtoCreateUpdate plan) throws UserNotFoundException {
        return this.createPlan(username, plan.getName(), plan.getDimensions());
    }

    /**
     * Updates a plan for a user
     * 
     * @param username - The username of the user for which a plan should be updated
     * @param id       - The Id of the Plan that should be updated
     * @param plan     - The updated Plan
     * @return PlanDtoDetail - The PlanDetailDto representation of the Plan after
     *         the update was executed
     * @throws UserNotFoundException       If no user with the username could be
     *                                     found
     * @throws EntityDoesNotExistException If no plan with that id could be found
     *                                     for the user
     */
    @Override
    @Transactional
    public PlanDtoDetail updatePlan(String username, long id, @Valid PlanDtoCreateUpdate plan)
            throws UserNotFoundException, EntityDoesNotExistException {
        ImmutableUserPlan user = this.findUser(username);
        Plan existingPlan = planRepository.find(user, id);
        existingPlan.setDimension(plan.getDimensions());
        existingPlan.setName(plan.getName());
        PlanDtoDetail updatedPlan = this.planConverter.detailDtoFrom(existingPlan);
        return updatedPlan;
    }

    /**
     * Deletes a plan from a user
     * 
     * @param username - The username of the user for which a plan should be updated
     * @param id       - The id of the Plan to be deleted
     * @throws UserNotFoundException       If no user with the username could be
     *                                     found
     * @throws EntityDoesNotExistException If no plan with that id could be found
     *                                     for the user
     */
    @Override
    @Transactional
    public void deletePlan(String username, long id) throws UserNotFoundException, EntityDoesNotExistException {
        ImmutableUserPlan user = this.findUser(username);
        planRepository.delete(user, id);
    }

    /**
     * Finds a page of all plans that match the filters for a user
     * 
     * @param username       - The username of the user for which plans should be
     *                       found.
     * @param filterName     - The string every name in the results has to contain.
     *                       If this is null then no plan will be matched. If this
     *                       is an emty String then all names will be matched
     * @param filterDateFrom - The earliest Date (Inclusive) every date in the
     *                       results has. If this is null it will be replaced with
     *                       {@link de.hsos.roomplanner.util.date.DateUtil#MIN_DATE}
     * @param filterDateTo   - The latest Date (Inclusive) every date in the results
     *                       has.If this is null it will be replaced with
     *                       {@link de.hsos.roomplanner.util.date.DateUtil#MIN_DATE}
     * @param pageSize       - The page size of the page to be returned
     * @param pageIndex      - The page index of the page to be returned
     * @return Page<PlanDtoListing> - A Page of the results
     * @throws UserNotFoundException If no user with the username could be found
     */
    @Override
    @Transactional
    public Page<PlanDtoListing> findPlans(
            String username,
            String filterName,
            LocalDate filterDateFrom,
            LocalDate filterDateTo,
            int pageSize,
            int pageIndex
    ) throws UserNotFoundException {
        ImmutableUserPlan user = this.findUser(username);

        if (filterDateFrom == null) {
            filterDateFrom = DateUtil.MIN_DATE;
        }
        if (filterDateTo == null) {
            filterDateTo = DateUtil.MAX_DATE;
        }

        Page<Plan> plans = planRepository.findAll(user, filterName, filterDateFrom, filterDateTo, pageSize, pageIndex);
        return new Page<PlanDtoListing>(
                plans.getTotalCount(),
                plans.getData().stream().map(planConverter::listingDtoFrom).collect(Collectors.toList())
        );
    }

    /**
     * Finds a specific Plan for a user
     * 
     * @param username - The username of the user for which th plan should be found
     * @param id       - The Id of the Plan to be retrieved
     * @return PlanDtoDetail - The PlanDtoDetail representation of the Plan found
     * @throws UserNotFoundException       If no user with the username could be
     *                                     found
     * @throws EntityDoesNotExistException If no plan with that id could be found
     *                                     for the user
     */
    @Override
    @Transactional
    public PlanDtoDetail findPlan(String username, long id) throws UserNotFoundException, EntityDoesNotExistException {
        ImmutableUserPlan user = this.findUser(username);
        return planConverter.detailDtoFrom(planRepository.find(user, id));
    }

    /**
     * 
     * @param username  - The username of the user for which a plan to be created
     * @param name      - The name of the Plan to be created
     * @param dimension - The dimensions of the Plan to be created
     * @return PlanDtoDetail - The PlanDtoDetail representation of the Plan created
     * @throws UserNotFoundException If no user with the username could be found
     * 
     * @see {@link #createPlan(String, PlanDtoCreateUpdate)}
     */
    @Override
    @Transactional
    public PlanDtoDetail createPlan(String username, String name, Dimension dimension) throws UserNotFoundException {
        ImmutableUserPlan user = this.findUser(username);
        Plan newPlan = new Plan(name, dimension);
        newPlan.setUser(user);
        planRepository.persist(newPlan);
        return planConverter.detailDtoFrom(newPlan);
    }

    /**
     * @param username  - The username of the user for which a plan should be
     *                  updated
     * @param id        - The id of the Plan to be updated
     * @param name      - The updated name of the plan
     * @param dimension - The updated dimensions of the Plan
     * @return PlanDtoDetail - The PlanDtoDetail representation of the Plan after
     *         the update
     * @throws UserNotFoundException       If no user with the username could be
     *                                     found
     * @throws EntityDoesNotExistException If no plan with that id could be found
     *                                     for the user
     */
    @Override
    @Transactional
    public PlanDtoDetail updatePlan(String username, long id, String name, Dimension dimension)
            throws UserNotFoundException, EntityDoesNotExistException {
        ImmutableUserPlan user = this.findUser(username);
        Plan existingPlan = planRepository.find(user, id);
        existingPlan.setDimension(dimension);
        existingPlan.setName(name);
        return planConverter.detailDtoFrom(existingPlan);
    }

    /**
     * @param username  - The username of the User for which a FurnitureInPlan
     *                  Object should be added to a Plan
     * @param planId    - The Id of the plan for which a FurnitureInPlan should be
     *                  created
     * @param furniture - The FurnitureInPlan object that should be created
     * @return FurnitureInPlanDto - The FurnitureInPlanDto representation of the
     *         furnitureInPlan object that was created.
     * @throws EntityDoesNotExistException If no plan with that id could be found
     *                                     for the user or if no furniture with the
     *                                     id provided in the furniture parameter
     *                                     could be found for the user
     * @throws UserNotFoundException       If no user with the username could be
     *                                     found
     */
    @Override
    @Transactional
    public FurnitureInPlanDto createFurnitureInPlan(
            String username,
            long planId,
            FurnitureInPlanDtoCreateUpdate furniture
    ) throws EntityDoesNotExistException, UserNotFoundException {
        ImmutableUserPlan user = this.findUser(username);
        ImmutableFurniture immutableFurniture = furnitureRepository.findFurniture(user, furniture.getFurnitureId());
        Plan plan = planRepository.find(user, planId);
        FurnitureInPlan furnitureInPlan = new FurnitureInPlan();
        furnitureInPlan.setFurniture(immutableFurniture);
        furnitureInPlan.setPlan(plan);
        furnitureInPlan.setPosition(furniture.getPosition());
        furnitureRepository.persist(furnitureInPlan);
        return furnitureConverter.dtoFrom(furnitureInPlan);
    }

    /**
     * Updates a FurnitureInPlan object
     * 
     * @param username          - The username of the user of the Plan in wich to
     *                          update the FutureInPlan object
     * @param planId            - The Id of the Plan in which to update the
     *                          FurnitureInPlan object
     * @param furnitureInPlanId - The Id of the FurnitureInPlan object to update
     * @param furniture         - The to update the FurnitureInPlan object
     * @return FurnitureInPlanDto - The FurnitureInPlanDto representation of the
     *         update FurnitureInPlan object
     * @throws EntityDoesNotExistException If no plan with that planId could be
     *                                     found for the user of if no
     *                                     FurnitureInPlan object with that
     *                                     furnitureInPlanId could be found for that
     *                                     plan or if no furniture with the id
     *                                     provided in the furniture parameter could
     *                                     be found for the user
     * @throws UserNotFoundException       If no user with the username could be
     *                                     found
     */
    @Override
    @Transactional
    public FurnitureInPlanDto updateFurnitureInPlan(
            String username,
            long planId,
            long furnitureInPlanId,
            FurnitureInPlanDtoCreateUpdate furniture
    ) throws UserNotFoundException, EntityDoesNotExistException {
        ImmutableUserPlan user = this.userRepository.findUser(username);
        Plan plan = planRepository.find(user, planId);
        FurnitureInPlan furnitureInPlan = furnitureRepository.findFurnitureInPlan(plan, furnitureInPlanId);
        if (furnitureInPlan.getFurniture().getId() != furniture.getFurnitureId()) {
            ImmutableFurniture immutableFurniture = furnitureRepository.findFurniture(user, furniture.getFurnitureId());
            furnitureInPlan.setFurniture(immutableFurniture);
        }
        furnitureInPlan.setPosition(furniture.getPosition());
        return furnitureConverter.dtoFrom(furnitureInPlan);
    }

    /**
     * Updates a FurnitureInPlan object
     * 
     * @param username          - The username of the user of the Plan in wich to
     *                          delete the FutureInPlan object
     * @param planId            - The Id of the Plan in which to delete the
     *                          FurnitureInPlan object
     * @param furnitureInPlanId - The Id of the FurnitureInPlan object to delete
     * @throws EntityDoesNotExistException If no plan with that planId could be
     *                                     found for the user of if no
     *                                     FurnitureInPlan object with that
     *                                     furnitureInPlanId could be found for that
     *                                     plan
     * @throws UserNotFoundException       If no user with the username could be
     *                                     found
     */
    @Override
    @Transactional
    public void deleteFurnitureInPlan(String username, long planId, long furnitureInPlanId)
            throws UserNotFoundException, EntityDoesNotExistException {
        ImmutableUserPlan user = this.findUser(username);
        Plan plan = this.planRepository.find(user, planId);
        this.furnitureRepository.deleteFurnitureFromPlan(plan, furnitureInPlanId);
    }

    /**
     * A private helper Method to retrieve a user from a username
     * 
     * @param username - The name of the user to be retrieved
     * @return ImmutableUserPlan - The user found
     * @throws UserNotFoundException If the user can not be found
     */
    private ImmutableUserPlan findUser(String username) throws UserNotFoundException {
        try {
            return userRepository.findUser(username);
        } catch (EntityDoesNotExistException ex) {
            throw new UserNotFoundException(username);
        }
    }

}

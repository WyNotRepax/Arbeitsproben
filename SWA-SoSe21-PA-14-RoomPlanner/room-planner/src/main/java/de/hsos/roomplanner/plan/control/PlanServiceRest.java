package de.hsos.roomplanner.plan.control;

import java.time.LocalDate;

import javax.validation.Valid;

import de.hsos.roomplanner.plan.control.dto.FurnitureInPlanDto;
import de.hsos.roomplanner.plan.control.dto.FurnitureInPlanDtoCreateUpdate;
import de.hsos.roomplanner.plan.control.dto.PlanDtoCreateUpdate;
import de.hsos.roomplanner.plan.control.dto.PlanDtoDetail;
import de.hsos.roomplanner.plan.control.dto.PlanDtoListing;
import de.hsos.roomplanner.util.Page;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;
import de.hsos.roomplanner.util.exception.UserNotFoundException;

/**
 * @author Benno Steinkamp
 */
public interface PlanServiceRest {

        Page<PlanDtoListing> findPlans(
                        String username,
                        String filterName,
                        LocalDate filterDateFrom,
                        LocalDate filterDateTo,
                        int pageSize,
                        int pageIndex
        ) throws UserNotFoundException;

        PlanDtoDetail findPlan(String username, long id) throws UserNotFoundException, EntityDoesNotExistException;

        PlanDtoDetail createPlan(String username, @Valid PlanDtoCreateUpdate plan) throws UserNotFoundException;

        PlanDtoDetail updatePlan(String username, long id, @Valid PlanDtoCreateUpdate plan)
                        throws UserNotFoundException, EntityDoesNotExistException;

        void deletePlan(String username, long id) throws UserNotFoundException, EntityDoesNotExistException;

        FurnitureInPlanDto createFurnitureInPlan(
                        String username,
                        long planId,
                        FurnitureInPlanDtoCreateUpdate furniture
        ) throws UserNotFoundException, EntityDoesNotExistException;

        FurnitureInPlanDto updateFurnitureInPlan(
                        String username,
                        long planId,
                        long furnitureInPlanId,
                        @Valid FurnitureInPlanDtoCreateUpdate furniture
        ) throws UserNotFoundException, EntityDoesNotExistException;

        void deleteFurnitureInPlan(String username, long planId, long furnitureInPlanId) throws UserNotFoundException, EntityDoesNotExistException;

}

package de.hsos.roomplanner.plan.control;

import java.time.LocalDate;

import de.hsos.roomplanner.plan.control.dto.PlanDtoDetail;
import de.hsos.roomplanner.plan.control.dto.PlanDtoListing;
import de.hsos.roomplanner.util.Dimension;
import de.hsos.roomplanner.util.Page;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;
import de.hsos.roomplanner.util.exception.UserNotFoundException;

/**
 * @author Benno Steinkamp
 */
public interface PlanServiceHttp {

    Page<PlanDtoListing> findPlans(
            String username,
            String filterName,
            LocalDate filterDateFrom,
            LocalDate filterDateTo,
            int pageSize,
            int pageIndex
    ) throws UserNotFoundException;

    PlanDtoDetail findPlan(String username, long id) throws UserNotFoundException, EntityDoesNotExistException;

    PlanDtoDetail createPlan(String username, String name, Dimension dimension) throws UserNotFoundException;

    PlanDtoDetail updatePlan(String username, long id, String name, Dimension dimension) throws UserNotFoundException, EntityDoesNotExistException;

    void deletePlan(String username, long id) throws UserNotFoundException, EntityDoesNotExistException;

}

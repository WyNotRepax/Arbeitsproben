package de.hsos.roomplanner.plan.control.dto;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.hsos.roomplanner.plan.entity.Plan;

/**
 * @author Benno Steinkamp
 * @author Christoph Freimuth
 */
@Singleton
public class PlanConverter {

    @Inject
    FurnitureConverter furnitureConverter;

    /**
     * Converts a plan entity to the corresponding Dto for listing request.
     * 
     * @param plan - the plan entity to be converted.
     * @return the corresponding PlanDtoListing
     */
    public PlanDtoListing listingDtoFrom(Plan plan) {
        if(plan == null){
            return null;
        }
        PlanDtoListing planDtoListing = new PlanDtoListing();
        setListingDtoValues(planDtoListing, plan);
        return planDtoListing;
    }

    /**
     * Converts a plan entity to the corresponding Dto for detail request.
     * 
     * @param plan - the plan entity to be converted.
     * @return the corresponding PlanDtoDetail.
     */
    public PlanDtoDetail detailDtoFrom(Plan plan) {
        if(plan == null){
            return null;
        }
        PlanDtoDetail planDtoDetail = new PlanDtoDetail();
        setDetailDtoValues(planDtoDetail, plan);
        return planDtoDetail;
    }

    /**
     * Sets the values of a listing Dto.
     * 
     * @param planDtoListing - Dto in which values are to be set.
     * @param plan           - plan entity from which values are used.
     */
    private void setListingDtoValues(PlanDtoListing planDtoListing, Plan plan) {
        planDtoListing.setId(plan.getId());
        planDtoListing.setName(plan.getName());
        planDtoListing.setDimension(plan.getDimension());
        planDtoListing.setDate(plan.getDate());
    }

    /**
     * Sets the values of a detail Dto.
     * 
     * @param planDtoDetail - Dto in which values are to be set.
     * @param plan          - plan entity from which values are used.
     */
    private void setDetailDtoValues(PlanDtoDetail planDtoDetail, Plan plan) {
        setListingDtoValues(planDtoDetail, plan);
        planDtoDetail.setFurniture(
                plan.getFurnitureInPlan().stream().map(furnitureConverter::dtoFrom).collect(Collectors.toList())
        );
    }

}

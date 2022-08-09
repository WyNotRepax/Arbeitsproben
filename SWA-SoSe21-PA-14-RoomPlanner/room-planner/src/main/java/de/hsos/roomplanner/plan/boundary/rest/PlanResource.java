package de.hsos.roomplanner.plan.boundary.rest;

import java.time.LocalDate;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

import de.hsos.roomplanner.plan.control.PlanServiceRest;
import de.hsos.roomplanner.plan.control.dto.FurnitureInPlanDtoCreateUpdate;
import de.hsos.roomplanner.plan.control.dto.PlanDtoCreateUpdate;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;
import de.hsos.roomplanner.util.exception.UserNotFoundException;
import io.quarkus.security.identity.SecurityIdentity;

/**
 * @author Christoph Freimuth
 */
@Path("api/v1/plan")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("user")
public class PlanResource {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    PlanServiceRest planService;

    @Inject
    Logger logger;

    /**
     * 
     * @param filterName     - the name to filter by.
     * @param filterDateFrom - the start date to filter by.
     * @param filterDateTo   - the end date to filter by.
     * @param pageSize       - the size of the page to be returned.
     * @param pageIndex      - the 0 starting index of the page of results to be
     *                       returned.
     * @return response with status OK with the page of results, or with status
     *         INTERNAL_SERVER_ERROR if the User does not exist.
     */
    @GET
    public Response getPlans(
            @QueryParam("filter[name]") @DefaultValue("") String filterName,
            @QueryParam("filter[dateFrom]") LocalDate filterDateFrom,
            @QueryParam("filter[dateTo]") LocalDate filterDateTo,
            @QueryParam("page[size]") @DefaultValue("5") int pageSize,
            @QueryParam("page[index]") @DefaultValue("0") int pageIndex
    ) {
        try {
            return Response
                    .ok(
                            this.planService.findPlans(
                                    this.securityIdentity.getPrincipal().getName(),
                                    filterName,
                                    filterDateFrom,
                                    filterDateTo,
                                    pageSize,
                                    pageIndex
                            )
                    )
                    .build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 
     * @param id - the ID of the plan to get.
     * @return response with status OK, or with status NOT_FOUND if the Entity does
     *         not exist, or with status INTERNAL_SERVER_ERROR if the User does not
     *         exist.
     */
    @GET
    @Path("{id}")
    public Response getPlan(@PathParam("id") long id) {
        try {
            return Response.ok(this.planService.findPlan(this.securityIdentity.getPrincipal().getName(), id)).build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.NOT_FOUND).entity(ex).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 
     * @param plan - the plan to be posted.
     * @return response with status CREATED, of with status INTERNAL_SERVER_ERROR if
     *         the User does not exist.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPlan(PlanDtoCreateUpdate plan) {
        try {

            return Response.status(Status.CREATED)
                    .entity(this.planService.createPlan(this.securityIdentity.getPrincipal().getName(), plan))
                    .build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 
     * @param id   - the ID of the plan to be changed.
     * @param plan - the dto which contains the changes.
     * @return response with status OK with the updated plan, or with status
     *         NOT_FOUND if the Entity does not exist, or with status
     *         INTERNAL_SERVER_ERROR if the User does not exist.
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putPlan(@PathParam("id") long id, PlanDtoCreateUpdate plan) {
        try {
            return Response.ok(this.planService.updatePlan(this.securityIdentity.getPrincipal().getName(), id, plan))
                    .build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.NOT_FOUND).entity(ex).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 
     * @param id - the ID of the plan to be deleted.
     * @return response with status OK if the planService returns true on the delete
     *         request, or status NOT_FOUND if the Entity does not exist, or with
     *         status INTERNAL_SERVER_ERROR if the User does not exist.
     */
    @DELETE
    @Path("{id}")
    public Response deletePlan(@PathParam("id") long id) {
        try {
            this.planService.deletePlan(this.securityIdentity.getPrincipal().getName(), id);
            return Response.ok().build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.NOT_FOUND).entity(ex).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 
     * @param planId    - the ID of the plan in which furniture is to be added.
     * @param furniture - the furniture which is to be added to the plan.
     * @return response with status CREATED, or with status NOT_FOUND if the Enitity
     *         does not exist, or with status INTERNAL_SERVER_ERROR id the User does
     *         not exist
     */
    @POST
    @Path("{id}/furniture")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postFurnitureInPlan(@PathParam("id") long planId, FurnitureInPlanDtoCreateUpdate furniture) {
        try {
            return Response.status(Status.CREATED)
                    .entity(
                            this.planService.createFurnitureInPlan(
                                    this.securityIdentity.getPrincipal().getName(),
                                    planId,
                                    furniture
                            )
                    )
                    .build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.NOT_FOUND).entity(ex).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 
     * @param planId            - the ID of the plan in which furniture is to be
     *                          changed.
     * @param funritureInPlanId - the ID of the furniture to be deleted
     * @param furniture         - the changed furniture.
     * @return response with status OK with the updated furnitureInPlanDto or with
     *         status NOT_FOUND if the Entity does not exist, or with status
     *         INTERNAL_SERVER_ERROR if the User does not exist.
     */
    @PUT
    @Path("{planId}/furniture/{furnitureInPlanId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putFurnitureInPlan(
            @PathParam("planId") long planId,
            @PathParam("furnitureInPlanId") long furnitureInPlanId,
            FurnitureInPlanDtoCreateUpdate furniture
    ) {
        try {
            return Response
                    .ok(
                            this.planService.updateFurnitureInPlan(
                                    this.securityIdentity.getPrincipal().getName(),
                                    planId,
                                    furnitureInPlanId,
                                    furniture
                            )
                    )
                    .build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.NOT_FOUND).entity(ex).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 
     * @param planId            - the ID of the plan is which funriture is to be
     *                          deleted.
     * @param furnitureInPlanId - the ID of the furniture to be deleted.
     * @return response with status OK, or with status NOT_FOUND if the Entity does
     *         not exist, or with status INTERNAL_SERVER_ERROR if User does not
     *         exist.
     */
    @DELETE
    @Path("{planId}/furniture/{furnitureInPlanId}")
    public Response deleteFurnitureInPlan(
            @PathParam("planId") long planId,
            @PathParam("furnitureInPlanId") long furnitureInPlanId
    ) {
        try {
            this.planService
                    .deleteFurnitureInPlan(this.securityIdentity.getPrincipal().getName(), planId, furnitureInPlanId);
            return Response.ok().build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.NOT_FOUND).entity(ex).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}

package de.hsos.roomplanner.plan.boundary.http;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import de.hsos.roomplanner.plan.control.PlanServiceHttp;
import de.hsos.roomplanner.plan.control.dto.PlanDtoDetail;
import de.hsos.roomplanner.plan.control.dto.PlanDtoListing;
import de.hsos.roomplanner.util.Dimension;
import de.hsos.roomplanner.util.Page;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;
import de.hsos.roomplanner.util.exception.UserNotFoundException;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.identity.SecurityIdentity;

/**
 * @author Christoph Freimuth
 */
@RequestScoped
@Path("plan")
@Produces(MediaType.TEXT_HTML)
@RolesAllowed("user")
public class PlanResource {

    public static enum Action {
        UPDATE,
        DELETE
    }

    @Inject
    PlanServiceHttp planService;

    @Inject
    SecurityIdentity securityIdentity;

    /**
     * Templates used in the templating-engine.
     */
    @CheckedTemplate
    public static class Templates {

        public static native TemplateInstance plans(
                List<PlanDtoListing> plans,
                int pageIndex,
                String nextPageHref,
                String prevPageHref,
                boolean hasNextPage,
                boolean hasPrevPage
        );

        public static native TemplateInstance plan(PlanDtoDetail plan);

    }

    /**
     * 
     * @param filterName     - the name to filter by.
     * @param filterDateFrom - the start date to filter by.
     * @param filterDateTo   - the end date to filter by.
     * @param pageSize       - the size of the page to be returned.
     * @param pageIndex      - the 0 starting index of the page of results to be
     *                       returned.
     * @return response with status OK with the resulting data and template, or with
     *         status INTERNAL_SERVER_ERROR if the User does not exist.
     */
    @GET
    public Response getPlans(
            @QueryParam("filter[name]") @DefaultValue("") String filterName,
            @QueryParam("filter[dateFrom]") LocalDate filterDateFrom,
            @QueryParam("filter[dateTo]") LocalDate filterDateTo,
            @QueryParam("page[size]") @DefaultValue("5") int pageSize,
            @QueryParam("page[index]") @DefaultValue("0") int pageIndex
    ) {
        Page<PlanDtoListing> page;
        try {
            page = this.planService.findPlans(
                    this.securityIdentity.getPrincipal().getName(),
                    filterName,
                    filterDateFrom,
                    filterDateTo,
                    pageSize,
                    pageIndex
            );
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        long totalCount = page.getTotalCount();
        int nextPageIndex = pageIndex + 1;
        int prevPageIndex = pageIndex - 1;
        boolean hasNextPage = nextPageIndex * pageSize < totalCount;
        boolean hasPrevPage = prevPageIndex >= 0;

        UriBuilder nextPageHrefBuilder = UriBuilder.fromResource(PlanResource.class)
                .queryParam("filter[name]", filterName)
                .queryParam("page[size]", pageSize)
                .queryParam("page[index]", nextPageIndex);

        UriBuilder prevPageHrefBuilder = UriBuilder.fromResource(PlanResource.class)
                .queryParam("filter[name]", filterName)
                .queryParam("page[size]", pageSize)
                .queryParam("page[index]", prevPageIndex);

        if (filterDateFrom != null) {
            nextPageHrefBuilder.queryParam("filter[dateFrom]", filterDateFrom);
            prevPageHrefBuilder.queryParam("filter[dateFrom]", filterDateFrom);
        }

        if (filterDateTo != null) {
            nextPageHrefBuilder.queryParam("filter[dateTo]", filterDateTo);
            prevPageHrefBuilder.queryParam("filter[dateTo]", filterDateTo);
        }

        return Response
                .ok(
                        Templates.plans(
                                page.getData(),
                                pageIndex,
                                nextPageHrefBuilder.build().toString(),
                                prevPageHrefBuilder.build().toString(),
                                hasNextPage,
                                hasPrevPage
                        )
                )
                .build();
    }

    /**
     * 
     * @param id - the ID of the plan to get.
     * @return response with status OK with the resulting data and template, or with
     *         status NOT_FOUND if the Entity does not exist, or with status
     *         INTERNAL_SERVER_ERROR if the User does not exist.
     */
    @GET
    @Path("{id}")
    public Response getPlan(@PathParam("id") long id) {
        try {

            return Response
                    .ok(Templates.plan(this.planService.findPlan(this.securityIdentity.getPrincipal().getName(), id)))
                    .build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.NOT_FOUND).entity(ex.getMessage()).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 
     * @param name   - the name value for a new plan.
     * @param width  - the width value for a new plan.
     * @param height - the height value for a new plan.
     * @param action
     * @return response with status OK with the resulting data and template, of with
     *         status INTERNAL_SERVER_ERROR if the User does not exist.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postPlan(
            @FormParam("name") String name,
            @FormParam("dimensionWidth") float width,
            @FormParam("dimensionHeight") float height
    ) {
        Dimension dimension = new Dimension(width, height);
        try {

            this.planService.createPlan(this.securityIdentity.getPrincipal().getName(), name, dimension);
            return Response.seeOther(UriBuilder.fromResource(PlanResource.class).build()).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response putDeleteOnAction(
            @PathParam("id") long id,
            @FormParam("action") Action action,
            @FormParam("name") String name,
            @FormParam("dimensionWidth") float width,
            @FormParam("dimensionHeight") float height
    ) {
        switch (action) {
        case DELETE:
            return this.deletePlan(id);
        case UPDATE:
            return this.putPlan(id, name, width, height);
        default:
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * 
     * @param id     - the ID of the plan to be changed.
     * @param name   - the new name of the plan.
     * @param width  - the new width of the plan.
     * @param height - the new height of the plan.
     * @param action
     * @return response with status OK with the resulting data and template, or with
     *         status NOT_FOUND if the Entity does not exist, or with status
     *         INTERNAL_SERVER_ERROR if the User does not exist.
     */
    public Response putPlan(
            @PathParam("id") long id,
            @FormParam("name") String name,
            @FormParam("dimensionWidth") float width,
            @FormParam("dimensionHeight") float height
    ) {
        Dimension dimension = new Dimension(width, height);
        try {

            this.planService.updatePlan(this.securityIdentity.getPrincipal().getName(), id, name, dimension);
            return Response.seeOther(UriBuilder.fromResource(PlanResource.class).build()).build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.NOT_FOUND).entity(ex.getMessage()).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 
     * @param id - the ID of the plan to be deleted.
     * @return response with status OK if the planService returns true on the delete
     *         request, or status BAD_REQUEST if its false, or with status
     *         INTERNAL_SERVER_ERROR if the User does not exist.
     */
    public Response deletePlan(@PathParam("id") long id) {
        try {
            this.planService.deletePlan(this.securityIdentity.getPrincipal().getName(), id);
            return Response.seeOther(UriBuilder.fromResource(PlanResource.class).build()).build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

    }

}

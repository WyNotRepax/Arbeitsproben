package de.hsos.roomplanner.furniture.boundary.http;

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

import de.hsos.roomplanner.furniture.control.FurnitureServiceHttp;
import de.hsos.roomplanner.furniture.control.dto.FurnitureDto;
import de.hsos.roomplanner.util.Dimension;
import de.hsos.roomplanner.util.Page;
import de.hsos.roomplanner.util.color.Color;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;
import de.hsos.roomplanner.util.exception.UserNotFoundException;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.identity.SecurityIdentity;

/**
 * @author Christoph Freimuth
 */

@RequestScoped
@Path("furniture")
@RolesAllowed("user")
@Produces(MediaType.TEXT_HTML)
public class FurnitureResource {

    public static enum Action {
        UPDATE,
        DELETE
    }

    @Inject
    FurnitureServiceHttp furnitureService;

    @Inject
    SecurityIdentity securityIdentity;

    /**
     * Templates used in the templating-engine
     */
    @CheckedTemplate
    public static class Templates {

        public static native TemplateInstance furnitures(
                List<FurnitureDto> furnitures,
                int pageIndex,
                String nextPageHref,
                String prevPageHref,
                boolean hasNextPage,
                boolean hasPrevPage
        );

        public static native TemplateInstance furniture(FurnitureDto furniture);

    }

    /**
     * 
     * @param filterName - the name to filter by.
     * @param pageSize   - the size of the page to be returned.
     * @param pageIndex  - the 0 starting index of the page of results to be
     *                   returned.
     * @return response with status ok with the resulting data and template.
     */
    @GET
    public Response getFurnitures(
            @QueryParam("filter[name]") @DefaultValue("") String filterName,
            @QueryParam("page[size]") @DefaultValue("5") int pageSize,
            @QueryParam("page[index]") @DefaultValue("0") int pageIndex
    ) {
        Page<FurnitureDto> page;
        try {
            page = this.furnitureService
                    .findFurnitures(securityIdentity.getPrincipal().getName(), filterName, pageSize, pageIndex);
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        long totalCount = page.getTotalCount();
        int nextPageIndex = pageIndex + 1;
        int prevPageIndex = pageIndex - 1;
        boolean hasNextPage = nextPageIndex * pageSize < totalCount;
        boolean hasPrevPage = prevPageIndex >= 0;

        String nextPageHref = UriBuilder.fromResource(FurnitureResource.class)
                .queryParam("filter[name]", filterName)
                .queryParam("page[size]", pageSize)
                .queryParam("page[index]", nextPageIndex)
                .build()
                .toString();

        String prevPageHref = UriBuilder.fromResource(FurnitureResource.class)
                .queryParam("filter[name]", filterName)
                .queryParam("page[size]", pageSize)
                .queryParam("page[index]", prevPageIndex)
                .build()
                .toString();

        return Response.ok(
                Templates.furnitures(page.getData(), pageIndex, nextPageHref, prevPageHref, hasNextPage, hasPrevPage)
        ).build();
    }

    /**
     * 
     * @param id - the ID of the furniture to get.
     * @return response with status NOT_FOUND if the result is null, or status ok
     *         with the resulting data and template if the result is not null.
     */
    @GET
    @Path("{id}")
    public Response getFurniture(@PathParam("id") long id) {
        try {
            FurnitureDto furniture = this.furnitureService
                    .findFurniture(this.securityIdentity.getPrincipal().getName(), id);
            return Response.ok(Templates.furniture(furniture)).build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.NOT_FOUND).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * 
     * @param name   - the name value for a new furniture.
     * @param width  - the width value for a new furniture.
     * @param height - the height value for a new furniture.
     * @param color  - the color value for a new furniture.
     * @return response with status BAD_REQUEST id result is null, or status ok with
     *         the resulting data and template if the result is not null.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postFurniture(
            @FormParam("name") String name,
            @FormParam("dimensionWidth") float width,
            @FormParam("dimensionHeight") float height,
            @FormParam("color") Color color
    ) {
        try {
            Dimension dimension = new Dimension(width, height);
            this.furnitureService
                    .createFurniture(this.securityIdentity.getPrincipal().getName(), name, dimension, color);
            return Response.seeOther(UriBuilder.fromResource(FurnitureResource.class).build()).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 
     * @param id     - the ID of the furniture to be changed or deleted.
     * @param action - the action to be performed (DELETE or UPDATE)
     * @param name   - the name of the furniture to be updated.
     * @param width  - the width of the furniture to be updated.
     * @param height - the height of the furniture to be updated.
     * @param color  - the color of the furniture to be updated.
     */
    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response putDeleteOnAction(
            @PathParam("id") long id,
            @FormParam("action") Action action,
            @FormParam("name") String name,
            @FormParam("dimensionWidth") float width,
            @FormParam("dimensionHeight") float height,
            @FormParam("color") Color color
    ) {
        switch (action) {
        case DELETE:
            return this.deleteFurniture(id);
        case UPDATE:
            return this.putFurniture(id, name, width, height, color);
        default:
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * 
     * @param id     - the ID of the furniture to be changed.
     * @param name   - the new name of the furniture.
     * @param width  - the new width of the furniture.
     * @param height - the new height of the furniture.
     * @param color  - the new color of the furniture.
     * 
     * @return response with status BAD_REQUEST id result is null, or status ok with
     *         the resulting data and template if the result is not null.
     */
    public Response putFurniture(
            @PathParam("id") long id,
            @FormParam("name") String name,
            @FormParam("dimensionWidth") float width,
            @FormParam("dimensionHeight") float height,
            @FormParam("color") Color color
    ) {
        Dimension dimension = new Dimension(width, height);
        try {
            this.furnitureService
                    .updateFurniture(this.securityIdentity.getPrincipal().getName(), id, name, dimension, color);
            return Response.seeOther(UriBuilder.fromResource(FurnitureResource.class).build()).build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * 
     * @param id - the ID of the furniture to be deleted.
     * @return response with status of if the furnitureService returns true on the
     *         delete request, or status BAD_REQUEST if its false.
     */
    public Response deleteFurniture(long id) {
        try {
            this.furnitureService.deleteFurniture(this.securityIdentity.getPrincipal().getName(), id);
            return Response.seeOther(UriBuilder.fromResource(FurnitureResource.class).build()).build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}

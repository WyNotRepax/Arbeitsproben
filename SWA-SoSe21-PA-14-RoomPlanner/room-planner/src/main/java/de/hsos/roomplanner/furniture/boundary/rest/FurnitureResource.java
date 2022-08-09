package de.hsos.roomplanner.furniture.boundary.rest;

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

import de.hsos.roomplanner.furniture.control.FurnitureServiceRest;
import de.hsos.roomplanner.furniture.control.dto.FurnitureDtoCreateUpdate;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;
import de.hsos.roomplanner.util.exception.UserNotFoundException;
import io.quarkus.security.identity.SecurityIdentity;

/**
 * @author Christoph Freimuth
 */
@Path("api/v1/furniture")
@RolesAllowed("user")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class FurnitureResource {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    FurnitureServiceRest furnitureService;

    /**
     * 
     * @param filterName - the name to filter by.
     * @param pageSize   - the size of the page to be returned.
     * @param pageIndex  - the 0 starting index of the page of result to be
     *                   returned.
     * @return response ok with the page of results.
     */
    @GET
    public Response getFurnitures(
            @QueryParam("filter[name]") @DefaultValue("") String filterName,
            @QueryParam("page[size]") @DefaultValue("5") int pageSize,
            @QueryParam("page[index]") @DefaultValue("0") int pageIndex
    ) {
        try {
            return Response
                    .ok(
                            this.furnitureService.findFurnitures(
                                    this.securityIdentity.getPrincipal().getName(),
                                    filterName,
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
     * @param id - the ID of the furniture to get.
     * @return response with status NOT_FOUND if the result is null, or status ok
     *         with the result if result is not null.
     */
    @GET
    @Path("{id}")
    public Response getFurniture(@PathParam("id") long id) {
        try {
            return Response.ok(this.furnitureService.findFurniture(this.securityIdentity.getPrincipal().getName(), id))
                    .build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.NOT_FOUND).entity(ex).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 
     * @param furniture - the furniture to be posted.
     * @return response with status BAD_REQUEST if result is null, or status ok with
     *         the result if result is not null.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postFurniture(FurnitureDtoCreateUpdate furniture) {
        try {
            return Response.status(Status.CREATED)
                    .entity(
                            this.furnitureService
                                    .createFurniture(this.securityIdentity.getPrincipal().getName(), furniture)
                    )
                    .build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * 
     * @param id        - the ID of the furniture to be changed.
     * @param furniture - the dto which contains the changes.
     * @return response with status ok with the updated furniture.
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putFurniture(@PathParam("id") long id, FurnitureDtoCreateUpdate furniture) {
        try {
            return Response
                    .ok(
                            this.furnitureService
                                    .updateFurniture(this.securityIdentity.getPrincipal().getName(), id, furniture)
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
     * @param id - the ID of the furniture to be deleted.
     * @return response with status ok if the furnitureService returns true on the
     *         delete request, or status BAD_REQUEST if its false.
     */
    @DELETE
    @Path("{id}")
    public Response deleteFurniture(@PathParam("id") long id) {
        try {
            this.furnitureService.deleteFurniture(this.securityIdentity.getPrincipal().getName(), id);
            return Response.ok().build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}

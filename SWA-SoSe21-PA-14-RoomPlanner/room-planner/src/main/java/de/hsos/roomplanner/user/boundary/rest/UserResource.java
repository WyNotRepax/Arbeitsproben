package de.hsos.roomplanner.user.boundary.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.hsos.roomplanner.user.control.UserServiceRest;
import de.hsos.roomplanner.user.control.dto.UserDto;
import de.hsos.roomplanner.util.exception.EntityAlreadyExistsException;

/**
 * @author Benno Steinkamp
 */
@Path("api/v1/user")
@RequestScoped
public class UserResource {

    @Inject
    UserServiceRest userServiceRest;

    /**
     * @param user
     * @return Response - A response with the status code 200 if a user was created
     *         otherwise a Response with status code 500
     */
    @POST
    public Response postUser(UserDto user) {
        try {
            userServiceRest.createUser(user);
            return Response.status(Status.CREATED).build();
        } catch (EntityAlreadyExistsException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex).build();
        }

    }

}

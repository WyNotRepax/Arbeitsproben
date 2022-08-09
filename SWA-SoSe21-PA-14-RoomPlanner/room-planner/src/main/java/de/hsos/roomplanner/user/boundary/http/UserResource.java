package de.hsos.roomplanner.user.boundary.http;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import de.hsos.roomplanner.user.control.UserServiceHttp;
import de.hsos.roomplanner.util.exception.EntityAlreadyExistsException;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

/**
 * @author Christoph Freimuth
 */

@RequestScoped
@Path("user")
@Produces(MediaType.TEXT_HTML)
public class UserResource {

    @Inject
    UserServiceHttp userServiceHttp;

    /**
     * Templates used in the templating-engine.
     */
    @CheckedTemplate
    public static class Templates {

        public static native TemplateInstance login(boolean error);

        public static native TemplateInstance register(boolean error);

    }

    /**
     * @param error weather or not to show the error message in the page
     * @return login template page.
     */
    @GET
    @Path("login")
    public Response getUserLogin(@QueryParam("error") @DefaultValue("false") boolean error) {
        return Response.ok(Templates.login(error)).build();
    }

    /**
     * @param error weather or not to show the error message in the page
     * @return register template page.
     */
    @GET
    @Path("register")
    public Response getUserRegister(@QueryParam("error") @DefaultValue("false") boolean error) {
        return Response.ok(Templates.register(error)).build();
    }

    /**
     * 
     * @param username - the username of the user to be created.
     * @param password - the password of the user to be created.
     * @return login template page if createUser is not false.
     */
    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postUser(@FormParam("username") String username, @FormParam("password") String password) {
        try {
            this.userServiceHttp.createUser(username, password);
            return Response
                    .seeOther(
                            UriBuilder.fromResource(UserResource.class).path(UserResource.class, "getUserLogin").build()
                    )
                    .build();
        } catch (EntityAlreadyExistsException ex) {
            return Response
                    .seeOther(
                            UriBuilder.fromResource(UserResource.class)
                                    .path(UserResource.class, "getUserRegister")
                                    .queryParam("error", true)
                                    .build()
                    )
                    .build();
        }
    }

}

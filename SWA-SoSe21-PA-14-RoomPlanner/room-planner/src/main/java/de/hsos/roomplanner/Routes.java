package de.hsos.roomplanner;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.UriBuilder;

import de.hsos.roomplanner.plan.boundary.http.PlanResource;
import io.quarkus.vertx.web.Route;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Benno Steinkamp
 */
@ApplicationScoped
public class Routes {

    
    /** 
     * Reroutes Requests to the route Path to the PlanResource
     * @param ctx - The Routing Context
     */
    @Route(methods = Route.HttpMethod.GET, path = "/")
    void indexRedirect(RoutingContext ctx) {
        ctx.redirect(UriBuilder.fromResource(PlanResource.class).build().toString());
    }

    /**
     * Allows the for Angular-Routing under /roomplanner-editor-wa/
     * @param ctx - The Routing Context
     */
    @Route(methods = Route.HttpMethod.GET, regex = "^/roomplanner-editor-wa/.+(?<!\\.(js|css|ico|png)(\\.map)?)$")
    void angularRoutes(RoutingContext ctx) { ctx.reroute("/roomplanner-editor-wa/"); }

}

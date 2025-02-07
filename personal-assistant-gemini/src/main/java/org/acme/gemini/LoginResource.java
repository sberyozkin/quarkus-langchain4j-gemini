package org.acme.gemini;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.oidc.IdToken;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

/**
 * Login resource
 */
@Path("/login")
@Authenticated
public class LoginResource {

    @Inject
    @IdToken
    JsonWebToken idToken;

    @Inject
    Template assistant;

    @GET
    @Produces("text/html")
    public TemplateInstance poem() {
        return assistant.data("name", idToken.getName());
    }
}

package org.acme.gemini;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/assistant")
@Authenticated
public class PersonalAssistantResource {

    PersonalAssistantService assistant;

    public PersonalAssistantResource(PersonalAssistantService assistant) {
        this.assistant = assistant;
    }

    @GET
    public String getPoem() {
        return assistant.writePoem();
    }
}

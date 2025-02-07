package org.acme.gemini;

import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

import dev.langchain4j.agent.tool.Tool;
import io.quarkus.oidc.token.propagation.AccessToken;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

//https://developers.google.com/calendar/api/v3/reference/events/list
//https://developers.google.com/calendar/api/v3/reference/events/list#examples

@RegisterRestClient(configKey="google-calendar-api")
@AccessToken
@Path("/calendars/primary")
public interface GoogleCalendarClient {

    @GET
    @Path("events")
    @Produces(MediaType.APPLICATION_JSON)
    @Tool("Get events")
    Events getEvents(@RestQuery("timeMin") String timeMin, @RestQuery("timeMax") String timeMax);
    
    public static record Events(List<Event> items) {
    }
    
    public static record Event(String summary, String description, String location, String kind, Start start, End end) {
    }
    
    public static record Start(String date, ZonedDateTime dateTime, String timeZone) {
    }
    public static record End(String date, ZonedDateTime dateTime, String timeZone) {
    }
}

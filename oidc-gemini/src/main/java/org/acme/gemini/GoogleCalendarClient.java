package org.acme.gemini;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

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
    @Tool("Get currently logged in user's calendar events with the information about the time zone, etc")
    Events getEvents();
    
    public static record Events(List<Event> items) {
    }
    
    public static record Event(String summary, String kind, Time start, Time end) {
    }
    
    public static record Time(String dateTime, String timeZone) {
    }
}

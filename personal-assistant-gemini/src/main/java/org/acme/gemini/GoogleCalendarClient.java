package org.acme.gemini;

import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

import dev.langchain4j.agent.tool.Tool;
import io.quarkus.oidc.token.propagation.AccessToken;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

//https://developers.google.com/calendar/api/v3/reference/events/list
//https://developers.google.com/calendar/api/v3/reference/events/list#examples

@RegisterRestClient(configKey = "google-calendar-api")
@AccessToken
@Path("/")
public interface GoogleCalendarClient {

    @GET
    @Path("/users/me/calendarList")
    @Produces(MediaType.APPLICATION_JSON)
    @Tool("Get calendars list")
    Calendars getCalendars();
    
    @GET
    @Path("/calendars/{calendarId}/events")
    @Produces(MediaType.APPLICATION_JSON)
    @Tool("Get events")
    Events getEvents(@PathParam("calendarId") String calendarId, @RestQuery("timeMin") String timeMin, @RestQuery("timeMax") String timeMax);
    
    @POST
    @Path("/calendars/{calendarId}/events")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Tool("Add event")
    Events addEvent(@PathParam("calendarId") String calendarId, Event event);
    
    @PUT
    @Path("/calendars/{calendarId}/events/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Tool("Update or move event")
    Events updateEvent(@PathParam("calendarId") String calendarId, @PathParam("eventId") String eventId, Event event);
    
    @DELETE
    @Path("/calendars/{calendarId}/events/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Tool("Delete event")
    void deleteEvent(@PathParam("calendarId") String calendarId, @PathParam("eventId") String eventId);

    
    public static record Calendars(List<Calendar> items) {
    }

    public static record Calendar(String id, String summary, String description, String location, String timeZone, boolean primary) {
    }
    
    public static record Events(List<Event> items) {
    }

    public static record Event(String summary, String description, String location, Start start, End end, String id) {
    }

    public static record Start(String date, ZonedDateTime dateTime, String timeZone) {
    }

    public static record End(String date, ZonedDateTime dateTime, String timeZone) {
    }
}

package org.acme.gemini;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import dev.langchain4j.agent.tool.Tool;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@WebSocket(path = "/assistant")
@Authenticated
public class PersonalAssistantResource {

    PersonalAssistantService assistant;

    public PersonalAssistantResource(PersonalAssistantService assistant) {
        this.assistant = assistant;
    }

    @Inject
    SecurityIdentity identity;

    @Inject
    @RestClient
    GoogleCalendarClient calendarClient;

    @OnOpen
    public String onOpen() {
        return "Hello, " + identity.getPrincipal().getName() + ", I'm your Personal Assistant, how can I help you?";
    }

    @OnTextMessage
    public String onMessage(String question) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        ZonedDateTime minDateTime = Instant.now().atZone(ZoneId.of("GMT"));
        String timeMin = minDateTime.format(formatter);

        ZonedDateTime maxDateTime = minDateTime.plusDays(30);
        String timeMax = maxDateTime.format(formatter);

        return assistant.assist(question, timeMin, timeMax);
    }

    @Singleton
    @Authenticated
    public static class SecurityTools {

        @Inject
        SecurityIdentity identity;

        @Tool("Returns the first and the family names of the logged-in user.")
        public String getLoggedInUserName() {
            return identity.getPrincipal().getName();
        }
        
        @Tool("Returns email address of the logged-in user.")
        public String getEmailAddressOfLoggedInUser() {
            return identity.getPrincipal().getName();
        }

    }
}

package org.acme.gemini;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import dev.langchain4j.agent.tool.Tool;
import io.quarkus.oidc.IdToken;
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
        @IdToken
        JsonWebToken identity; 

        @Tool("Returns the first name and the family name of the logged-in user.")
        public String getLoggedInUserName() {
            return identity.getName();
        }
        
        @Tool("Returns email address of the logged-in user.")
        public String getEmailAddressOfLoggedInUser() {
            return identity.getClaim(Claims.email);
        }

    }
}

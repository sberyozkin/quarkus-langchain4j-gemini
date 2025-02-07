package org.acme.gemini;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import io.quarkus.oidc.IdToken;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@WebSocket(path = "/assistant")
@Authenticated
public class PersonalAssistantResource {

    private static final Logger log = Logger.getLogger(PersonalAssistantResource.class);
    
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
    public static class PersonalAssistantTools {

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
    
    @Singleton
    public static class AssistantChatModelListener implements ChatModelListener {
        @Inject
        WebSocketConnection wsConnection;
        
        public void onResponse(ChatModelResponseContext context) {
            if (context.chatResponse().aiMessage().text() != null && context.chatResponse().aiMessage().hasToolExecutionRequests()) {
                log.info("Gemini provided text content while also requesting tool executions to complete the user query. Sending this text to the user now.");
                wsConnection.sendText(context.chatResponse().aiMessage().text());
            }
        }
    }
}

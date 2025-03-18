package org.acme.gemini;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.SessionScoped;

@RegisterAiService(tools = { GoogleCalendarClient.class })
@SessionScoped
public interface PersonalAssistantService {
    @UserMessage("""
            You are a professional poet.
            Your task is to write a poem about Java. 
             """)
    String writePoem();
}

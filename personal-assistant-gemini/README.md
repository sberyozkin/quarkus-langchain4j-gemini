# Personal Assistant Gemini Demo

This demo introduces Personal Assistant Gemini.

## OpenId Connect authentication

This demo requires users to authenticate with Google.

You have to register an application with Google, follow steps listed in the [Quarkus Google](https://quarkus.io/guides/security-openid-connect-providers#google) section.

Name your Google application as `Quarkus LangChain4j AI`, and make sure an allowed callback URL is set to `http://localhost:8080/login`.
Google will generate a client id and secret, use them to set `quarkus.oidc.client-id` and `quarkus.oidc.credentials.secret` properties.
You must also enable Vertex AI API in your Google Cloud project.

## Vertex AI Gemini

Vertex AI Gemini model is configured as follows:

```properties
quarkus.langchain4j.vertexai.gemini.location=europe-west2
quarkus.langchain4j.vertexai.gemini.project-id=${GOOGLE_PROJECT_ID}
quarkus.langchain4j.vertexai.gemini.log-requests=true
quarkus.langchain4j.vertexai.gemini.log-responses=true
```

Set `GOOGLE_PROJECT_ID` to the id of your Google Cloud project.

Note that the current user access token which is time constrained and can be refreshed will be used to access Gemini.
No API keys are configured.

## Running the Demo

To run the demo, use the following commands:

```shell
mvn quarkus:dev
```

Access `http://localhost:8080`, login to Quarkus with Google, and follow a provided application link and request Gemini to assist with providing information about your scheduled events and helping to schedule new events.

# OIDC Gemini Demo

This advanced secure poem demo showcases how users authenticated with Google can work with Vertex AI Gemini

## OpenId Connect authentication

This demo requires users to authenticate with Google.

You have to register an application with Google, follow steps listed in the [Quarkus Google](https://quarkus.io/guides/security-openid-connect-providers#google) section.

Name your Google application as `Quarkus LangChain4j AI`, and make sure an allowed callback URL is set to `http://localhost:8080/model/vertex-gemini`.
Google will generate a client id and secret, use them to set `quarkus.vertex-gemini.oidc.client-id` and `quarkus.vertex-gemini.oidc.credentials.secret` properties.
Set `GOOGLE_PROJECT_ID` to the id of your Google Cloud project.
You must also enable Vertex AI API in your Google Cloud project.

## Vertex AI Gemini

Vertex AI Gemini model is configured as follows:

```properties
quarkus.langchain4j.vertexai.gemini.location=europe-west2
quarkus.langchain4j.vertexai.gemini.project-id=${GOOGLE_PROJECT_ID}
quarkus.langchain4j.vertexai.gemini.log-requests=true
quarkus.langchain4j.vertexai.gemini.log-responses=true
```

Note that the current user access token which is time constrained and can be refreshed will be used to access Gemini.
No API keys are configured.

## Running the Demo

To run the demo, use the following commands:

```shell
mvn quarkus:dev
```

Access `http://localhost:8080`, login to Quarkus with Google, and follow a provided application link and request Gemini to write a poem using the currently logged in user's name and calendar event timezones. For example, you can expect to get something similar to:

```
Hello Sergey Beryozkin, please enjoy reading a poem about Dublin Time Zone.

Author: Gemini

Green isle afloat in emerald sea,
Your clock ticks differently for me.
Dublin time, a rhythm of its own,
Where ancient tales and laughter's sown. 

```


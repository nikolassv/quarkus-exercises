package com.example.uhoh;

import com.example.uhoh.model.Message;
import com.example.uhoh.model.Presence;
import com.example.uhoh.model.PresenceKind;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class UhOhTest {

    private static final Duration AWAIT = Duration.ofSeconds(3);

    @TestHTTPResource("/")
    URI baseUri;

    private MessagesClient messages;
    private PresenceClient presence;

    @BeforeEach
    void buildClients() {
        messages = RestClientBuilder.newBuilder().baseUri(baseUri).build(MessagesClient.class);
        presence = RestClientBuilder.newBuilder().baseUri(baseUri).build(PresenceClient.class);
    }

    @Test
    void subscriberReceivesPostedMessage() throws InterruptedException {
        AssertSubscriber<Message> sub = openStream(messages::stream);
        try {
            postMessage("alice", "is this thing on?");

            sub.awaitItems(1, AWAIT);
            Message m = sub.getItems().get(0);
            assertThat(m.sender()).isEqualTo("alice");
            assertThat(m.text()).isEqualTo("is this thing on?");
            assertThat(m.sentAt())
                    .as("the pipeline should stamp every message with a server-side sentAt")
                    .isNotNull();
        } finally {
            sub.cancel();
        }
    }

    @Test
    void messageTextIsTrimmed() throws InterruptedException {
        AssertSubscriber<Message> sub = openStream(messages::stream);
        try {
            postMessage("bob", "   spaces around me   ");

            sub.awaitItems(1, AWAIT);
            assertThat(sub.getItems().get(0).text())
                    .as("the pipeline should trim leading/trailing whitespace")
                    .isEqualTo("spaces around me");
        } finally {
            sub.cancel();
        }
    }

    @Test
    void blankMessagesAreDroppedAndStreamSurvives() throws InterruptedException {
        AssertSubscriber<Message> sub = openStream(messages::stream);
        try {
            postMessage("eve", "   ");
            postMessage("eve", "still here");

            sub.awaitItems(1, AWAIT);
            // Give any blank that slipped through a chance to arrive too.
            Thread.sleep(200);
            List<Message> items = sub.getItems();

            assertThat(items)
                    .as("the valid message after a blank one should still arrive")
                    .anyMatch(m -> "still here".equals(m.text()));
            assertThat(items)
                    .as("blank messages should be dropped, not forwarded")
                    .noneMatch(m -> m.text() == null || m.text().trim().isEmpty());
        } finally {
            sub.cancel();
        }
    }

    @Test
    void joinEventIsBroadcastToSubscribers() throws InterruptedException {
        AssertSubscriber<Presence> sub = openStream(presence::stream);
        try {
            postPresence("carol", "JOINED");

            sub.awaitItems(1, AWAIT);
            Presence p = sub.getItems().get(0);
            assertThat(p.sender()).isEqualTo("carol");
            assertThat(p.kind()).isEqualTo(PresenceKind.JOINED);
            assertThat(p.at())
                    .as("the pipeline should stamp every presence event with a server-side timestamp")
                    .isNotNull();
        } finally {
            sub.cancel();
        }
    }

    @Test
    void invalidPresenceKindIsDroppedAndStreamSurvives() throws InterruptedException {
        AssertSubscriber<Presence> sub = openStream(presence::stream);
        try {
            postPresence("intruder", "BARGE_IN");
            postPresence("carol", "JOINED");

            sub.awaitItems(1, AWAIT);
            assertThat(sub.getItems())
                    .as("a valid presence event after a malformed one should still arrive")
                    .anyMatch(p -> "carol".equals(p.sender()) && p.kind() == PresenceKind.JOINED);
        } finally {
            sub.cancel();
        }
    }

    @Test
    void nullMessageTextIsDroppedAndStreamSurvives() throws InterruptedException {
        AssertSubscriber<Message> sub = openStream(messages::stream);
        try {
            postRaw("/messages", "{\"sender\":\"frank\",\"text\":null}");
            postMessage("frank", "I made it through");

            sub.awaitItems(1, AWAIT);
            Thread.sleep(200);
            List<Message> items = sub.getItems();

            assertThat(items)
                    .as("a valid message posted after a null-text one should still arrive")
                    .anyMatch(m -> "I made it through".equals(m.text()));
            assertThat(items)
                    .as("messages with null text should be dropped, not forwarded")
                    .noneMatch(m -> m.text() == null);
        } finally {
            sub.cancel();
        }
    }

    @Test
    void nullPresenceKindIsDroppedAndStreamSurvives() throws InterruptedException {
        AssertSubscriber<Presence> sub = openStream(presence::stream);
        try {
            postRaw("/presence", "{\"sender\":\"ghost\",\"kind\":null}");
            postPresence("dave", "JOINED");

            sub.awaitItems(1, AWAIT);
            assertThat(sub.getItems())
                    .as("a valid presence event after a null-kind one should still arrive")
                    .anyMatch(p -> "dave".equals(p.sender()) && p.kind() == PresenceKind.JOINED);
        } finally {
            sub.cancel();
        }
    }

    private <T> AssertSubscriber<T> openStream(Supplier<Multi<T>> source) throws InterruptedException {
        AssertSubscriber<T> sub = source.get()
                .subscribe().withSubscriber(AssertSubscriber.create(Long.MAX_VALUE));
        // Let the SSE handshake complete and the server-side subscription register
        // in the broadcaster before any POST is published.
        Thread.sleep(150);
        return sub;
    }

    private void postMessage(String sender, String text) {
        given()
                .baseUri(baseUri.toString())
                .contentType("application/json")
                .body("{\"sender\":\"" + sender + "\",\"text\":\"" + text + "\"}")
                .when()
                .post("/messages")
                .then()
                .statusCode(202);
    }

    private void postPresence(String sender, String kind) {
        given()
                .baseUri(baseUri.toString())
                .contentType("application/json")
                .body("{\"sender\":\"" + sender + "\",\"kind\":\"" + kind + "\"}")
                .when()
                .post("/presence")
                .then()
                .statusCode(202);
    }

    private void postRaw(String path, String jsonBody) {
        given()
                .baseUri(baseUri.toString())
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post(path)
                .then()
                .statusCode(202);
    }

    @Path("/messages")
    public interface MessagesClient {
        @GET
        @Path("/stream")
        @Produces(MediaType.SERVER_SENT_EVENTS)
        @RestStreamElementType(MediaType.APPLICATION_JSON)
        Multi<Message> stream();
    }

    @Path("/presence")
    public interface PresenceClient {
        @GET
        @Path("/stream")
        @Produces(MediaType.SERVER_SENT_EVENTS)
        @RestStreamElementType(MediaType.APPLICATION_JSON)
        Multi<Presence> stream();
    }
}

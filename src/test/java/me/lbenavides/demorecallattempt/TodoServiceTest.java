package me.lbenavides.demorecallattempt;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * The TodoServiceTest class is responsible for testing the TodoService class.
 */
class TodoServiceTest {


    private MockWebServer server;

    private TodoService service;

    @BeforeEach
    void setUp() {
        server = new MockWebServer();
        WebClient client = WebClient.builder()
                .baseUrl(server.url("/")
                                 .toString())
                .build();


        service = new TodoService(client);
    }



    @Test
    @DisplayName("this is the happy path so it should work fine")
    void calledAndEverythingIsOk() {
        MockResponse response = ServerResponseMother.validResponse1();
        server.enqueue(response);


        List<Todo> todos = service.getAll()
                .collectList()
                .block();

        assertNotNull(todos);
        assertEquals(2, todos.size());
    }


    @Test
    @DisplayName("This is when we get an specific error that should be retried")
    void retryAfterAFail() {


        server.enqueue(ServerResponseMother.unAuthorizedResponse());
        server.enqueue(ServerResponseMother.validResponse2());


        List<Todo> todos = service.getAll()
                .collectList()
                .block();

        assertNotNull(todos);
        assertEquals(3, todos.size());
    }

    @Test
    @DisplayName("This is when we get an specific error that should be retried but after the limit of retry is reached then an exception is thrown")
    void retryAfter2Fails() {


        server.enqueue(ServerResponseMother.unAuthorizedResponse());
        server.enqueue(ServerResponseMother.unAuthorizedResponse());
        server.enqueue(ServerResponseMother.unAuthorizedResponse());
        server.enqueue(ServerResponseMother.validResponse2());


        assertThrows(RuntimeException.class, ()-> service.getAll()
                .collectList()
                .block());
    }

    @Test
    void dontRetryAfterOtherError() {

        server.enqueue(ServerResponseMother.serverError());
        server.enqueue(ServerResponseMother.validResponse2());


        assertThrows(RuntimeException.class, ()-> service.getAll()
                .collectList()
                .block());

    }

}
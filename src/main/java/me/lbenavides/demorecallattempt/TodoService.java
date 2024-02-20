package me.lbenavides.demorecallattempt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class TodoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TodoService.class);

    private final WebClient client;

    public TodoService(WebClient client) {
        this.client = client;
    }

    /**
     * Retrieves all the todos calling a server, and it retries given a specific case otherwise it will throw an exception.
     *
     * @return a Flux of Todo objects representing the todos
     */
    public Flux<Todo> getAll(){

        int retryAttempts = 2;

        return client.get().uri(uriBuilder -> uriBuilder.path("todos").build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                          clientResponse -> {

                              LOGGER.info("clientResponse.statusCode() -> {}", clientResponse.statusCode());

                              return Mono.error(new ClientUnauthorizedException(clientResponse.statusCode()
                                                                                        .value()));
                          })
                .bodyToFlux(Todo.class)
                .retryWhen(Retry.fixedDelay(retryAttempts, Duration.ofMillis(100)).filter(t-> t instanceof ClientUnauthorizedException));
    }
}
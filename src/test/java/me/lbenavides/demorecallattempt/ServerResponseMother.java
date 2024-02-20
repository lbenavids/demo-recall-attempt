package me.lbenavides.demorecallattempt;

import okhttp3.mockwebserver.MockResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;

public class ServerResponseMother {
    @NotNull
    static MockResponse validResponse1() {
        MockResponse response = new MockResponse();
        response.setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);
        response.setBody("""
                                 [
                                   {
                                         "userId": 1,
                                         "id": 1,
                                         "title": "delectus aut autem",
                                         "completed": false
                                     },
                                     {
                                         "userId": 1,
                                         "id": 2,
                                         "title": "quis ut nam facilis et officia qui",
                                         "completed": false
                                     }
                                     ]
                                 """);
        return response;
    }

    @NotNull
    static MockResponse validResponse2() {
        MockResponse response = new MockResponse();
        response.setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);
        response.setBody("""
                                   [
                                                             {
                                 "userId": 1,
                                 "id": 3,
                                 "title": "fugiat veniam minus",
                                 "completed": false}, 
                                 {
                                     "userId": 1,
                                     "id": 4,
                                     "title": "et porro tempora",
                                     "completed": true
                                 } ,
                                  {
                                         "userId": 1,
                                         "id": 5,
                                         "title": "laboriosam mollitia et enim quasi adipisci quia provident illum",
                                         "completed": false
                                     }                                                       
                                       ]
                                   """);
        return response;
    }

    static MockResponse unAuthorizedResponse() {
        return new MockResponse()
                .setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .setStatus("HTTP/1.1 401 Unauthorized")
                .setBody("""
                                             {
                                             "message": "You are not allowed unless you try again"
                                             }
                                 """);
    }

    static MockResponse serverError() {
        return new MockResponse()
                .setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .setStatus("HTTP/1.1 502 BAD_GATEWAY")
                .setBody("""
                                             {
                                             "message": "Something went Wrong"
                                             }
                                 """);
    }
}

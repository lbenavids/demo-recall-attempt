package me.lbenavides.demorecallattempt;

public class ClientUnauthorizedException extends RuntimeException{


    public ClientUnauthorizedException(int errorCode) {
        super("The server did not allow us to get data with an error %d".formatted(errorCode));
    }
}

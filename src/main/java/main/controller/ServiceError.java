package main.controller;

public class ServiceError {
    String message;
    String details;
    //add more data if needed

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public ServiceError() {
    }

    public ServiceError(String message) {
        this.message = message;
    }

    public ServiceError(String message, String details) {
        this.message = message;
        this.details = details;
    }



}

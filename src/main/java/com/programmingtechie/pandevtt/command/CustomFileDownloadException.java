package com.programmingtechie.pandevtt.command;

public class CustomFileDownloadException extends RuntimeException {
    public CustomFileDownloadException(String message) {
        super(message);
    }

    public CustomFileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}

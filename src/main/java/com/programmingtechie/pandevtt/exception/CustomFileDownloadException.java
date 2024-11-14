package com.programmingtechie.pandevtt.exception;

public class CustomFileDownloadException extends RuntimeException {

    /**
     * Конструктор для создания исключения с сообщением и причиной ошибки.
     *
     * @param message описание ошибки
     * @param cause причина возникновения ошибки (исключение, вызвавшее эту ошибку)
     */
    public CustomFileDownloadException(String message, Throwable cause) {
        // Вызов конструктора родительского класса RuntimeException с сообщением и причиной
        super(message, cause);
    }
}

package com.programmingtechie.pandevtt.command;

public interface Command {

    /**
     * Метод для выполнения команды.
     * Конкретные реализации команд будут предоставлять логику обработки команд.
     *
     * @param text    текст команды, который будет передан для обработки
     * @param chatId  идентификатор чата, необходимый для отправки ответа пользователю
     * @return строка с ответом на команду, которую будет отправлено пользователю
     */
    String execute(String text, Long chatId);
}

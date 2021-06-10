package ru.hse.kdz04.validator;

public interface ValidationError {
    /**
     * Возвращает описание ошибки для пользователя
     *
     * @return сообщение об ошибке
     */
    String getMessage();

    /**
     * Возвращает путь до значения, не прошедшего проверку
     *
     * @return путь до провалившегося значения
     */
    String getPath();

    /**
     * Возвращает объект, который не прошел проверку
     *
     * @return Объект, проваливший условия аннотации
     */
    Object getFailedValue();
}

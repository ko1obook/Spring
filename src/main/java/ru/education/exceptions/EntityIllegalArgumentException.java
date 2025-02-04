package ru.education.exceptions;

/**
 * Исключение выбрасывется при вызове сервиса с некорректными параметрами
 */
public class EntityIllegalArgumentException extends BaseException {

    public EntityIllegalArgumentException(String message) {
        super(message);
    }
}

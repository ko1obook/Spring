package ru.education.exceptions;

/**
 * Исключение выбрасывается при конфликте с существующими данными на основе заложенной бизнес-логики
 */
public class EntityConflictException extends BaseException {

    public EntityConflictException(String message) {
        super(message);
    }
}

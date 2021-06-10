package ru.hse.kdz04.analyzer;

import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.validator.ValidationErrorClass;

import java.lang.reflect.AnnotatedType;
import java.util.Optional;

public interface Analyzer {
    /**
     * Проверяет объект относительно требований у аннотации
     *
     * @param type аннотированный тип
     * @param obj  объект для проверки
     * @param path путь до ошибки
     * @return Ошибка типа ValidationErrorClass, обернутая в Optional, так как ошибки может не быть
     * @throws IncorrectAnnotatedTypeException возникает в результате применение
     *                                         аннотации к некорректному типу
     */
    Optional<ValidationErrorClass> analyzeObject(AnnotatedType type, Object obj, String path)
            throws IncorrectAnnotatedTypeException;
}

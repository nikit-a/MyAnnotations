package ru.hse.kdz04.analyzer;

import ru.hse.kdz04.validator.ValidationErrorClass;

import java.lang.reflect.AnnotatedType;
import java.util.Optional;

public class NotNullAnalyzer implements Analyzer {
    @Override
    public Optional<ValidationErrorClass> analyzeObject(AnnotatedType type, Object obj, String path) {
        if (obj == null) {
            return Optional.of(new ValidationErrorClass("Значение не должно быть null", path, null));
        }
        return Optional.empty();
    }
}

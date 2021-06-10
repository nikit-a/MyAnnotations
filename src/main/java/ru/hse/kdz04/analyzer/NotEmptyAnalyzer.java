package ru.hse.kdz04.analyzer;

import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.validator.ValidationErrorClass;

import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.Optional;

public class NotEmptyAnalyzer implements Analyzer {
    @Override
    public Optional<ValidationErrorClass> analyzeObject(AnnotatedType type, Object obj, String path)
            throws IncorrectAnnotatedTypeException {
        // Так как при значении null действие аннотации отменяется
        if (obj == null) {
            return Optional.empty();
        }
        boolean isEmpty;
        // Проверка на возможность каста к корректным типам
        if (obj instanceof Collection) {
            isEmpty = ((Collection<?>) obj).isEmpty();
        } else if (obj instanceof String) {
            isEmpty = ((String) obj).isEmpty();
        } else {
            throw new IncorrectAnnotatedTypeException("Объект не является типом " +
                    "String, Collection к которым применяется аннотация NotEmpty");
        }
        // Проверка на пустое значение
        if (isEmpty) {
            return Optional.of(new ValidationErrorClass("Значение не должно быть пустым", path, obj));
        }
        return Optional.empty();

    }
}

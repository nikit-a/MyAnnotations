package ru.hse.kdz04.analyzer;

import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.validator.ValidationErrorClass;

import java.lang.reflect.AnnotatedType;

import java.util.Optional;

public class NotBlankAnalyzer implements Analyzer {
    @Override
    public Optional<ValidationErrorClass> analyzeObject(AnnotatedType type, Object obj, String path)
            throws IncorrectAnnotatedTypeException {
        // Так как при значении null действие аннотации отменяется
        if (obj == null) {
            return Optional.empty();
        }
        boolean isBlank;
        // Проверка на возможность каста к корректному типу
        if (obj instanceof String) {
            isBlank = ((String) obj).isBlank();
        } else {
            throw new IncorrectAnnotatedTypeException("Объект не является типом " +
                    "String к которому применяется аннотация NotBlank");
        }
        // Проверка на содержание пробелов
        if (isBlank) {
            return Optional.of(new ValidationErrorClass("Значение не должно быть пустым или" +
                    " содержать white space codepoints", path, obj));
        }
        return Optional.empty();

    }

}

package ru.hse.kdz04.analyzer;

import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.validator.ValidationErrorClass;

import java.lang.reflect.AnnotatedType;
import java.util.Optional;

public class PositiveAnalyzer implements Analyzer {
    @Override
    public Optional<ValidationErrorClass> analyzeObject(AnnotatedType type, Object obj, String path)
            throws IncorrectAnnotatedTypeException {
        // Так как при значении null действие аннотации отменяется
        if (obj == null) {
            return Optional.empty();
        }
        // Проверка на возможность каста к корректным типам
        if (obj instanceof Byte || obj instanceof Short || obj instanceof Integer || obj instanceof Long) {
            Number num = (Number) obj;
            if (num.longValue() <= 0) {
                return Optional.of(new ValidationErrorClass("У числа " + num +
                        " не положительное значение", path, obj));
            }
        } else {
            throw new IncorrectAnnotatedTypeException("Объект не является типом " +
                    "byte, short, int, long и их обёртками к которым применяется аннотация Positive");
        }
        return Optional.empty();


    }
}

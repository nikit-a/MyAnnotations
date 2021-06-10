package ru.hse.kdz04.analyzer;

import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.validator.ValidationErrorClass;
import ru.hse.kdz04.annotation.InRange;

import java.lang.reflect.AnnotatedType;
import java.util.Optional;

public class InRangeAnalyzer implements Analyzer {
    @Override
    public Optional<ValidationErrorClass> analyzeObject(AnnotatedType type, Object obj, String path)
            throws IncorrectAnnotatedTypeException {
        // Так как при значении null действие аннотации отменяется
        if (obj == null) {
            return Optional.empty();
        }
        // Проверка на возможность каста к корректным типам
        if (obj instanceof Byte || obj instanceof Short || obj instanceof Integer || obj instanceof Long) {
            InRange inRange = type.getAnnotation(InRange.class);
            // Проверка на корректность указанных параметров аннотации
            if (inRange.max() < inRange.min()) {
                throw new IllegalArgumentException("Значение не может быть в промежутке [" +
                        inRange.min() + ";" + inRange.max() + "]");
            }
            Number num = (Number) obj;
            if (num.longValue() < inRange.min() || num.longValue() > inRange.max()) {
                return Optional.of(new ValidationErrorClass("Число " + num +
                        " не входит в диапазон [" + inRange.min() + ";" + inRange.max() + "]", path, obj));
            }
        } else {
            throw new IncorrectAnnotatedTypeException("Объект не является типом " +
                    "byte, short, int, long и их обёртками к которым применяется аннотация InRange");
        }

        return Optional.empty();
    }


}

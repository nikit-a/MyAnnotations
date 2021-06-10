package ru.hse.kdz04.analyzer;

import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.validator.ValidationErrorClass;
import ru.hse.kdz04.annotation.AnyOf;

import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.Optional;

public class AnyOfAnalyzer implements Analyzer {

    @Override
    public Optional<ValidationErrorClass> analyzeObject(AnnotatedType type, Object obj, String path)
            throws IncorrectAnnotatedTypeException {
        // Так как при значении null действие аннотации отменяется
        if (obj == null) {
            return Optional.empty();
        }
        // Проверка на возможность каста к корректному типу
        if (obj instanceof String) {
            AnyOf anyOf = type.getAnnotation(AnyOf.class);
            String objectStr = (String) obj;
            // Проверка, что строка совпадает с одной из заявленных в аннотации
            if (Arrays.stream(anyOf.str()).noneMatch(x -> x.equals(objectStr))) {
                return Optional.of(new ValidationErrorClass("Cтрока " + objectStr +
                        " не совпадает ни с одной подстрокой из массива: " +
                        Arrays.toString(anyOf.str()), path, obj));
            }
        } else {
            throw new IncorrectAnnotatedTypeException("Объект не является типом " +
                    "String к которому применяется аннотация AnyOf");
        }
        return Optional.empty();
    }

}

package ru.hse.kdz04.analyzer;

import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.validator.ValidationErrorClass;
import ru.hse.kdz04.annotation.Size;

import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.Optional;

public class SizeAnalyzer implements Analyzer {
    @Override
    public Optional<ValidationErrorClass> analyzeObject(AnnotatedType type, Object obj, String path)
            throws IncorrectAnnotatedTypeException {
        // Так как при значении null действие аннотации отменяется
        if (obj == null) {
            return Optional.empty();
        }
        int sizeElement;
        // Проверка на возможность каста к корректным типам
        if (obj instanceof Collection) {
            sizeElement = ((Collection<?>) obj).size();
        } else if (obj instanceof String) {
            sizeElement = ((String) obj).length();
        } else {
            throw new IncorrectAnnotatedTypeException("Объект не является типом " +
                    "String, Collection к которым применяется аннотация Size");
        }
        Size sizeAnnotation = type.getAnnotation(Size.class);
        // Проверка на корректность указанных параметров аннотации
        if (sizeAnnotation.max() < sizeAnnotation.min()) {
            throw new IllegalArgumentException("Размер не может быть в промежутке [" +
                    sizeAnnotation.min() + ";" + sizeAnnotation.max() + "]");
        }
        if (sizeElement < sizeAnnotation.min() || sizeElement > sizeAnnotation.max()) {
            return Optional.of(new ValidationErrorClass("Размер аннотированного элемента " + sizeElement +
                    " не входит в диапазон [" + sizeAnnotation.min() + ";" + sizeAnnotation.max() + "]", path, obj));
        }
        return Optional.empty();

    }
}

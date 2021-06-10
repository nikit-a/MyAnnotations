package ru.hse.kdz04.analyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.annotation.InRange;
import ru.hse.kdz04.annotation.Size;
import ru.hse.kdz04.validator.ValidationErrorClass;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClassTestSize {
    @Size(min = 4, max = 9)
    public String str = null;

    @Size(min = 1, max = 4)
    public Integer number = 3;

    @Size(min = 4, max = 3)
    public String firstName = "Nik";

    @Size(min = 1, max = 1000)
    public String person = "Nikita";

    @Size(min = 5, max = 10)
    public List<String> listNum = List.of("1", "2", "3", "4", "5");

    @Size(min = 1, max = 3)
    public String person2 = "Nikita";

    @Size(min = 3, max = 10)
    public List<String> listNum2 = List.of("1", "2");

}

class SizeAnalyzerTest {
    ClassTestSize obj = new ClassTestSize();

    @Test
    @DisplayName("Объект null под аннотацией Size")
    void sizeForNullObject() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[0].get(obj);
        AnnotatedType annotatedField = fields[0].getAnnotatedType();
        SizeAnalyzer sizeAnalyzer = new SizeAnalyzer();
        assertEquals(Optional.empty(), sizeAnalyzer.analyzeObject(annotatedField, fieldValue, ""));
    }

    @Test
    @DisplayName("Нарушение типа, у которого может находится аннотация Size")
    void sizeForIncorrectTypes() throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[1].get(obj);
        AnnotatedType annotatedField = fields[1].getAnnotatedType();
        SizeAnalyzer sizeAnalyzer = new SizeAnalyzer();
        IncorrectAnnotatedTypeException ex = assertThrows(IncorrectAnnotatedTypeException.class,
                () -> sizeAnalyzer.analyzeObject(annotatedField, fieldValue, ""));
        String errorMes = "Объект не является типом String, Collection к которым применяется аннотация Size";
        assertEquals(errorMes, ex.getMessage());
    }


    @Test
    @DisplayName("Некорректные параметры аннотации Size")
    void sizeForIncorrectParameters() throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[2].get(obj);
        AnnotatedType annotatedField = fields[2].getAnnotatedType();
        SizeAnalyzer sizeAnalyzer = new SizeAnalyzer();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sizeAnalyzer.analyzeObject(annotatedField, fieldValue, ""));
        String errorMes = "Размер не может быть в промежутке [4;3]";
        assertEquals(errorMes, ex.getMessage());
    }


    @Test
    @DisplayName("Корректное значение согласно аннотации Size")
    void sizeForCorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[3].get(obj);
        AnnotatedType annotatedField = fields[3].getAnnotatedType();
        SizeAnalyzer sizeAnalyzer = new SizeAnalyzer();
        assertEquals(Optional.empty(), sizeAnalyzer.analyzeObject(annotatedField, fieldValue, ""));

        Object fieldValue2 = fields[4].get(obj);
        AnnotatedType annotatedField2 = fields[4].getAnnotatedType();
        assertEquals(Optional.empty(), sizeAnalyzer.analyzeObject(annotatedField2, fieldValue2, ""));

    }


    @Test
    @DisplayName("Некорректное значение согласно аннотации Size")
    void sizeForIncorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[5].get(obj);
        AnnotatedType annotatedField = fields[5].getAnnotatedType();
        SizeAnalyzer sizeAnalyzer = new SizeAnalyzer();
        Optional<ValidationErrorClass> error = sizeAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        if (error.isPresent()) {
            Object failedValue = error.get().getFailedValue();
            assertEquals(fieldValue, failedValue);
        }
        Object fieldValue2 = fields[6].get(obj);
        AnnotatedType annotatedField2 = fields[6].getAnnotatedType();
        Optional<ValidationErrorClass> error2 = sizeAnalyzer.analyzeObject(annotatedField2, fieldValue2, "");
        if (error2.isPresent()) {
            Object failedValue2 = error2.get().getFailedValue();
            assertEquals(fieldValue2, failedValue2);
        }

    }
}
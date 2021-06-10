package ru.hse.kdz04.analyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.annotation.AnyOf;
import ru.hse.kdz04.annotation.Negative;
import ru.hse.kdz04.validator.ValidationErrorClass;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClassTestNegative {
    @Negative
    public Integer day = null;
    @Negative
    public String firstName = "Egor";

    @Negative
    public Integer counter = -1;

    @Negative
    public Long number = -10000000L;

    @Negative
    public Short height = 0;

    @Negative
    public Byte money = 100;

}

class NegativeAnalyzerTest {
    ClassTestNegative obj = new ClassTestNegative();

    @Test
    @DisplayName("Объект null под аннотацией Negative")
    void negativeForNullObject() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[0].get(obj);
        AnnotatedType annotatedField = fields[0].getAnnotatedType();
        NegativeAnalyzer negativeAnalyzer = new NegativeAnalyzer();
        Optional<ValidationErrorClass> optError;
        optError = negativeAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        assertEquals(Optional.empty(), optError);
    }


    @Test
    @DisplayName("Нарушение типа, у которого может находится аннотация Negative")
    void negativeForIncorrectTypes() throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[1].get(obj);
        AnnotatedType annotatedField = fields[1].getAnnotatedType();
        NegativeAnalyzer negativeAnalyzer = new NegativeAnalyzer();
        IncorrectAnnotatedTypeException ex = assertThrows(IncorrectAnnotatedTypeException.class,
                () -> negativeAnalyzer.analyzeObject(annotatedField, fieldValue, ""));
        String errorMes = "Объект не является типом byte, short, int, long и их обёртками" +
                " к которым применяется аннотация Negative";
        assertEquals(errorMes, ex.getMessage());
    }


    @Test
    @DisplayName("Корректное значение согласно аннотации Negative")
    void negativeForCorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[2].get(obj);
        AnnotatedType annotatedField = fields[2].getAnnotatedType();
        NegativeAnalyzer negativeAnalyzer = new NegativeAnalyzer();
        assertEquals(Optional.empty(), negativeAnalyzer.analyzeObject(annotatedField, fieldValue, ""));

        Object fieldValue2 = fields[3].get(obj);
        AnnotatedType annotatedField2 = fields[3].getAnnotatedType();
        assertEquals(Optional.empty(), negativeAnalyzer.analyzeObject(annotatedField2, fieldValue2, ""));

    }


    @Test
    @DisplayName("Некорректное значение согласно аннотации Negative")
    void negativeForIncorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[4].get(obj);
        AnnotatedType annotatedField = fields[4].getAnnotatedType();
        NegativeAnalyzer negativeAnalyzer = new NegativeAnalyzer();
        Optional<ValidationErrorClass> error = negativeAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        if (error.isPresent()) {
            Object failedValue = error.get().getFailedValue();
            assertEquals(fieldValue, failedValue);
        }
        Object fieldValue2 = fields[5].get(obj);
        AnnotatedType annotatedField2 = fields[5].getAnnotatedType();
        Optional<ValidationErrorClass> error2 = negativeAnalyzer.analyzeObject(annotatedField2, fieldValue2, "");
        if (error2.isPresent()) {
            Object failedValue2 = error2.get().getFailedValue();
            assertEquals(fieldValue2, failedValue2);
        }

    }
}
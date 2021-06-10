package ru.hse.kdz04.analyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.annotation.Negative;
import ru.hse.kdz04.annotation.Positive;
import ru.hse.kdz04.validator.ValidationErrorClass;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClassTestPositive {
    @Positive
    public Integer day = null;
    @Positive
    public String firstName = "Egor";

    @Positive
    public Integer counter = 1;

    @Positive
    public Long number = 10000000L;

    @Positive
    public Short height = 0;

    @Positive
    public Byte money = -100;

}

class PositiveAnalyzerTest {
    ClassTestPositive obj = new ClassTestPositive();

    @Test
    @DisplayName("Объект null под аннотацией Positive")
    void positiveForNullObject() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[0].get(obj);
        AnnotatedType annotatedField = fields[0].getAnnotatedType();
        PositiveAnalyzer positiveAnalyzer = new PositiveAnalyzer();
        Optional<ValidationErrorClass> optError;
        optError = positiveAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        assertEquals(Optional.empty(), optError);
    }


    @Test
    @DisplayName("Нарушение типа, у которого может находится аннотация Positive")
    void positiveForIncorrectTypes() throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[1].get(obj);
        AnnotatedType annotatedField = fields[1].getAnnotatedType();
        PositiveAnalyzer positiveAnalyzer = new PositiveAnalyzer();
        IncorrectAnnotatedTypeException ex = assertThrows(IncorrectAnnotatedTypeException.class,
                () -> positiveAnalyzer.analyzeObject(annotatedField, fieldValue, ""));
        String errorMes = "Объект не является типом byte, short, int, long и их обёртками" +
                " к которым применяется аннотация Positive";
        assertEquals(errorMes, ex.getMessage());
    }


    @Test
    @DisplayName("Корректное значение согласно аннотации Positive")
    void positiveForCorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[2].get(obj);
        AnnotatedType annotatedField = fields[2].getAnnotatedType();
        PositiveAnalyzer positiveAnalyzer = new PositiveAnalyzer();
        assertEquals(Optional.empty(), positiveAnalyzer.analyzeObject(annotatedField, fieldValue, ""));

        Object fieldValue2 = fields[3].get(obj);
        AnnotatedType annotatedField2 = fields[3].getAnnotatedType();
        assertEquals(Optional.empty(), positiveAnalyzer.analyzeObject(annotatedField2, fieldValue2, ""));

    }


    @Test
    @DisplayName("Некорректное значение согласно аннотации Positive")
    void positiveForIncorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[4].get(obj);
        AnnotatedType annotatedField = fields[4].getAnnotatedType();
        PositiveAnalyzer positiveAnalyzer = new PositiveAnalyzer();
        Optional<ValidationErrorClass> error = positiveAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        if (error.isPresent()) {
            Object failedValue = error.get().getFailedValue();
            assertEquals(fieldValue, failedValue);
        }
        Object fieldValue2 = fields[5].get(obj);
        AnnotatedType annotatedField2 = fields[5].getAnnotatedType();
        Optional<ValidationErrorClass> error2 = positiveAnalyzer.analyzeObject(annotatedField2, fieldValue2, "");
        if (error2.isPresent()) {
            Object failedValue2 = error2.get().getFailedValue();
            assertEquals(fieldValue2, failedValue2);
        }

    }
}
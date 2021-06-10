package ru.hse.kdz04.analyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.annotation.InRange;
import ru.hse.kdz04.validator.ValidationErrorClass;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClassTestInRange {
    @InRange(min = 4, max = 9)
    public Integer counter = null;
    @InRange(min = 4, max = 9)
    public String str = "hello";

    @InRange(min = 4, max = 3)
    public Integer number = 7;

    @InRange(min = 1, max = 10000000000L)
    public Long person = 100000000L;

    @InRange(min = 1, max = 10)
    public Byte kol = 10;

    @InRange(min = -100, max = 99)
    public Integer day = 100;

    @InRange(min = -3, max = 4)
    public Integer sum = -4;
}

class InRangeAnalyzerTest {
    ClassTestInRange obj = new ClassTestInRange();

    @Test
    @DisplayName("Объект null под аннотацией InRange")
    void inRangeForNullObject() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[0].get(obj);
        AnnotatedType annotatedField = fields[0].getAnnotatedType();
        InRangeAnalyzer inRangeAnalyzer = new InRangeAnalyzer();
        assertEquals(Optional.empty(), inRangeAnalyzer.analyzeObject(annotatedField, fieldValue, ""));
    }

    @Test
    @DisplayName("Нарушение типа, у которого может находится аннотация InRange")
    void inRangeForIncorrectTypes() throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[1].get(obj);
        AnnotatedType annotatedField = fields[1].getAnnotatedType();
        InRangeAnalyzer inRangeAnalyzer = new InRangeAnalyzer();
        IncorrectAnnotatedTypeException ex = assertThrows(IncorrectAnnotatedTypeException.class,
                () -> inRangeAnalyzer.analyzeObject(annotatedField, fieldValue, ""));
        String errorMes = "Объект не является типом byte, short, int, long и их обёртками" +
                " к которым применяется аннотация InRange";
        assertEquals(errorMes, ex.getMessage());
    }


    @Test
    @DisplayName("Некорректные параметры аннотации InRange")
    void inRangeForIncorrectParameters() throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[2].get(obj);
        AnnotatedType annotatedField = fields[2].getAnnotatedType();
        InRangeAnalyzer inRangeAnalyzer = new InRangeAnalyzer();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> inRangeAnalyzer.analyzeObject(annotatedField, fieldValue, ""));
        String errorMes = "Значение не может быть в промежутке [4;3]";
        assertEquals(errorMes, ex.getMessage());
    }


    @Test
    @DisplayName("Корректное значение согласно аннотации InRange")
    void inRangeForCorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[3].get(obj);
        AnnotatedType annotatedField = fields[3].getAnnotatedType();
        InRangeAnalyzer inRangeAnalyzer = new InRangeAnalyzer();
        assertEquals(Optional.empty(), inRangeAnalyzer.analyzeObject(annotatedField, fieldValue, ""));

        Object fieldValue2 = fields[4].get(obj);
        AnnotatedType annotatedField2 = fields[4].getAnnotatedType();
        assertEquals(Optional.empty(), inRangeAnalyzer.analyzeObject(annotatedField2, fieldValue2, ""));

    }


    @Test
    @DisplayName("Некорректное значение согласно аннотации InRange")
    void inRangeForIncorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[5].get(obj);
        AnnotatedType annotatedField = fields[5].getAnnotatedType();
        InRangeAnalyzer inRangeAnalyzer = new InRangeAnalyzer();
        Optional<ValidationErrorClass> error = inRangeAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        if (error.isPresent()) {
            Object failedValue = error.get().getFailedValue();
            assertEquals(fieldValue, failedValue);
        }
        Object fieldValue2 = fields[6].get(obj);
        AnnotatedType annotatedField2 = fields[6].getAnnotatedType();
        Optional<ValidationErrorClass> error2 = inRangeAnalyzer.analyzeObject(annotatedField2, fieldValue2, "");
        if (error2.isPresent()) {
            Object failedValue2 = error2.get().getFailedValue();
            assertEquals(fieldValue2, failedValue2);
        }

    }
}
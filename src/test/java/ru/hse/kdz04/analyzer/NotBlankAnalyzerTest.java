package ru.hse.kdz04.analyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.annotation.AnyOf;
import ru.hse.kdz04.annotation.NotBlank;
import ru.hse.kdz04.validator.ValidationErrorClass;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClassTestNotBlank {
    @NotBlank
    public String name = null;

    @NotBlank
    public int number = 9;

    @NotBlank
    public List<String> lst = List.of("1", "2", "Al");

    @NotBlank
    public String name2 = " re";

    @NotBlank
    public String name3 = "Egor";

    @NotBlank
    public String name4 = "       ";

    @NotBlank
    public String name5 = "";
}

class NotBlankAnalyzerTest {
    ClassTestNotBlank obj = new ClassTestNotBlank();

    @Test
    @DisplayName("Объект null под аннотацией NotBlank")
    void notBlankForNullObject() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[0].get(obj);
        AnnotatedType annotatedField = fields[0].getAnnotatedType();
        NotBlankAnalyzer notBlankAnalyzer = new NotBlankAnalyzer();
        Optional<ValidationErrorClass> optError;
        optError = notBlankAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        assertEquals(Optional.empty(), optError);
    }


    @Test
    @DisplayName("Нарушение типа, у которого может находится аннотация NotBlank")
    void notBlankIncorrectTypes() throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[1].get(obj);
        AnnotatedType annotatedField = fields[1].getAnnotatedType();
        NotBlankAnalyzer notBlankAnalyzer = new NotBlankAnalyzer();
        IncorrectAnnotatedTypeException ex = assertThrows(IncorrectAnnotatedTypeException.class,
                () -> notBlankAnalyzer.analyzeObject(annotatedField, fieldValue, ""));
        String errorMes = "Объект не является типом String к которому применяется аннотация NotBlank";
        assertEquals(errorMes, ex.getMessage());

        Object fieldValue2 = fields[2].get(obj);
        AnnotatedType annotatedField2 = fields[2].getAnnotatedType();
        IncorrectAnnotatedTypeException ex2 = assertThrows(IncorrectAnnotatedTypeException.class,
                () -> notBlankAnalyzer.analyzeObject(annotatedField2, fieldValue2, ""));
        assertEquals(errorMes, ex2.getMessage());
    }


    @Test
    @DisplayName("Корректное значение согласно аннотации NotBlank")
    void notBlankForCorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[3].get(obj);
        AnnotatedType annotatedField = fields[3].getAnnotatedType();
        NotBlankAnalyzer notBlankAnalyzer = new NotBlankAnalyzer();
        notBlankAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        notBlankAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        assertEquals(Optional.empty(), notBlankAnalyzer.analyzeObject(annotatedField, fieldValue, ""));


        Object fieldValue2 = fields[4].get(obj);
        AnnotatedType annotatedField2 = fields[4].getAnnotatedType();
        assertEquals(Optional.empty(), notBlankAnalyzer.analyzeObject(annotatedField2, fieldValue2, ""));
    }

    @Test
    @DisplayName("Некорректное значение согласно аннотации NotBlank")
    void notBlankForIncorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {

        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[5].get(obj);
        AnnotatedType annotatedField = fields[5].getAnnotatedType();
        NotBlankAnalyzer notBlankAnalyzer = new NotBlankAnalyzer();
        Optional<ValidationErrorClass> error = notBlankAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        if (error.isPresent()) {
            Object failedValue = error.get().getFailedValue();
            assertEquals(fieldValue, failedValue);
        }
        Object fieldValue2 = fields[6].get(obj);
        AnnotatedType annotatedField2 = fields[6].getAnnotatedType();
        Optional<ValidationErrorClass> error2 = notBlankAnalyzer.analyzeObject(annotatedField2, fieldValue2, "");
        if (error2.isPresent()) {
            Object failedValue2 = error2.get().getFailedValue();
            assertEquals(fieldValue2, failedValue2);
        }

    }
}
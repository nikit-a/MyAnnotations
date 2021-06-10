package ru.hse.kdz04.analyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.annotation.NotBlank;
import ru.hse.kdz04.annotation.NotEmpty;
import ru.hse.kdz04.validator.ValidationErrorClass;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClassTestNotEmpty {
    @NotEmpty
    public String name = null;

    @NotEmpty
    public int number = 9;

    @NotEmpty
    public short day = 15;

    @NotEmpty
    public List<String> name2 = List.of("1", "2");

    @NotEmpty
    public String name3 = "       ";

    @NotEmpty
    public String name4 = "";

}

class NotEmptyAnalyzerTest {
    ClassTestNotEmpty obj = new ClassTestNotEmpty();

    @Test
    @DisplayName("Объект null под аннотацией NotEmpty")
    void notEmptyForNullObject() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[0].get(obj);
        AnnotatedType annotatedField = fields[0].getAnnotatedType();
        NotEmptyAnalyzer notEmptyAnalyzer = new NotEmptyAnalyzer();
        Optional<ValidationErrorClass> optError;
        optError = notEmptyAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        assertEquals(Optional.empty(), optError);
    }


    @Test
    @DisplayName("Нарушение типа, у которого может находится аннотация NotEmpty")
    void notEmptyIncorrectTypes() throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[1].get(obj);
        AnnotatedType annotatedField = fields[1].getAnnotatedType();
        NotEmptyAnalyzer notEmptyAnalyzer = new NotEmptyAnalyzer();
        IncorrectAnnotatedTypeException ex = assertThrows(IncorrectAnnotatedTypeException.class,
                () -> notEmptyAnalyzer.analyzeObject(annotatedField, fieldValue, ""));
        String errorMes = "Объект не является типом String, Collection к которым применяется аннотация NotEmpty";
        assertEquals(errorMes, ex.getMessage());

        Object fieldValue2 = fields[2].get(obj);
        AnnotatedType annotatedField2 = fields[2].getAnnotatedType();
        IncorrectAnnotatedTypeException ex2 = assertThrows(IncorrectAnnotatedTypeException.class,
                () -> notEmptyAnalyzer.analyzeObject(annotatedField2, fieldValue2, ""));
        assertEquals(errorMes, ex2.getMessage());
    }


    @Test
    @DisplayName("Корректное значение согласно аннотации NotEmpty")
    void notEmptyForCorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[3].get(obj);
        AnnotatedType annotatedField = fields[3].getAnnotatedType();
        NotEmptyAnalyzer notEmptyAnalyzer = new NotEmptyAnalyzer();
        notEmptyAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        notEmptyAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        assertEquals(Optional.empty(), notEmptyAnalyzer.analyzeObject(annotatedField, fieldValue, ""));


        Object fieldValue2 = fields[4].get(obj);
        AnnotatedType annotatedField2 = fields[4].getAnnotatedType();
        assertEquals(Optional.empty(), notEmptyAnalyzer.analyzeObject(annotatedField2, fieldValue2, ""));
    }

    @Test
    @DisplayName("Некорректное значение согласно аннотации NotEmpty")
    void notEmptyForIncorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {

        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[5].get(obj);
        AnnotatedType annotatedField = fields[5].getAnnotatedType();
        NotEmptyAnalyzer notEmptyAnalyzer = new NotEmptyAnalyzer();
        Optional<ValidationErrorClass> error = notEmptyAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        if (error.isPresent()) {
            Object failedValue = error.get().getFailedValue();
            assertEquals(fieldValue, failedValue);
        }
    }
}
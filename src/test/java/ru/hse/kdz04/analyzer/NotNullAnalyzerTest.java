package ru.hse.kdz04.analyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.annotation.NotNull;
import ru.hse.kdz04.validator.ValidationErrorClass;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClassTestNotNull {
    @NotNull
    public String name = null;
    @NotNull
    public int number = 9;
}

class NotNullAnalyzerTest {
    ClassTestNotNull obj = new ClassTestNotNull();

    @Test
    @DisplayName("Объект null под аннотацией NotNull")
    void notNullForNullObject() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[0].get(obj);
        AnnotatedType annotatedField = fields[0].getAnnotatedType();
        NotNullAnalyzer notNullAnalyzer = new NotNullAnalyzer();
        Optional<ValidationErrorClass> error = notNullAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        if (error.isPresent()) {
            Object failedValue = error.get().getFailedValue();
            assertEquals(fieldValue, failedValue);
        }
    }


    @Test
    @DisplayName("Объект не null под аннотацией NotNull")
    void notNullForNotNullObject() throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[1].get(obj);
        AnnotatedType annotatedField = fields[1].getAnnotatedType();
        NotNullAnalyzer notNullAnalyzer = new NotNullAnalyzer();
        Optional<ValidationErrorClass> optError;
        optError = notNullAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        assertEquals(Optional.empty(), optError);

    }
}
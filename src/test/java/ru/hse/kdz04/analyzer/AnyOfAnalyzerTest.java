package ru.hse.kdz04.analyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.annotation.AnyOf;
import ru.hse.kdz04.validator.ValidationErrorClass;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClassTestAnyOf {
    @AnyOf(str = {"Alex", "Nikita"})
    public String name = null;

    @AnyOf(str = {"1", "2"})
    public int number = 9;

    @AnyOf(str = {"Al", "Ser"})
    public List<String> lst = List.of("1", "2", "Al");

    @AnyOf(str = {"Alex", "Nikita"})
    public String name2 = "Alex";

    @AnyOf(str = {"al", "wt", "as"})
    public String name3 = "as";

    @AnyOf(str = {"al", "wt", "aes"})
    public String name4 = "ae";

    @AnyOf(str = {"al", "wt", "as"})
    public String name5 = "As";
}

class AnyOfAnalyzerTest {
    ClassTestAnyOf obj = new ClassTestAnyOf();

    @Test
    @DisplayName("Объект null под аннотацией AnyOf")
    void anyOfForNullObject() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[0].get(obj);
        AnnotatedType annotatedField = fields[0].getAnnotatedType();
        AnyOfAnalyzer anyOfAnalyzer = new AnyOfAnalyzer();
        Optional<ValidationErrorClass> optError;
        optError = anyOfAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        assertEquals(Optional.empty(), optError);
    }


    @Test
    @DisplayName("Нарушение типа, у которого может находится аннотация AnyOf")
    void anyOfForIncorrectTypes() throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[1].get(obj);
        AnnotatedType annotatedField = fields[1].getAnnotatedType();
        AnyOfAnalyzer anyOfAnalyzer = new AnyOfAnalyzer();
        IncorrectAnnotatedTypeException ex = assertThrows(IncorrectAnnotatedTypeException.class,
                () -> anyOfAnalyzer.analyzeObject(annotatedField, fieldValue, ""));
        String errorMes = "Объект не является типом String к которому применяется аннотация AnyOf";
        assertEquals(errorMes, ex.getMessage());

        Object fieldValue2 = fields[2].get(obj);
        AnnotatedType annotatedField2 = fields[2].getAnnotatedType();
        IncorrectAnnotatedTypeException ex2 = assertThrows(IncorrectAnnotatedTypeException.class,
                () -> anyOfAnalyzer.analyzeObject(annotatedField2, fieldValue2, ""));
        assertEquals(errorMes, ex2.getMessage());
    }


    @Test
    @DisplayName("Корректное значение согласно аннотации AnyOf")
    void anyOfForCorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[3].get(obj);
        AnnotatedType annotatedField = fields[3].getAnnotatedType();
        AnyOfAnalyzer anyOfAnalyzer = new AnyOfAnalyzer();
        anyOfAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        assertEquals(Optional.empty(), anyOfAnalyzer.analyzeObject(annotatedField, fieldValue, ""));


        Object fieldValue2 = fields[4].get(obj);
        AnnotatedType annotatedField2 = fields[4].getAnnotatedType();
        assertEquals(Optional.empty(), anyOfAnalyzer.analyzeObject(annotatedField2, fieldValue2, ""));
    }

    @Test
    @DisplayName("Некорректное значение согласно аннотации AnyOf")
    void anyOfForIncorrectValues() throws IllegalAccessException, IncorrectAnnotatedTypeException {

        Field[] fields = obj.getClass().getDeclaredFields();
        Object fieldValue = fields[5].get(obj);
        AnnotatedType annotatedField = fields[5].getAnnotatedType();
        AnyOfAnalyzer anyOfAnalyzer = new AnyOfAnalyzer();
        Optional<ValidationErrorClass> error = anyOfAnalyzer.analyzeObject(annotatedField, fieldValue, "");
        if (error.isPresent()) {
            Object failedValue = error.get().getFailedValue();
            assertEquals(fieldValue, failedValue);
        }
        Object fieldValue2 = fields[6].get(obj);
        AnnotatedType annotatedField2 = fields[6].getAnnotatedType();

        Optional<ValidationErrorClass> error2 = anyOfAnalyzer.analyzeObject(annotatedField2, fieldValue2, "");
        if (error2.isPresent()) {
            Object failedValue2 = error2.get().getFailedValue();
            assertEquals(fieldValue2, failedValue2);
        }

    }

}
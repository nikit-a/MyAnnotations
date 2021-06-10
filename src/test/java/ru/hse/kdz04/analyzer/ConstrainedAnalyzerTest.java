package ru.hse.kdz04.analyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hse.kdz04.annotation.Constrained;

import static org.junit.jupiter.api.Assertions.*;

@Constrained
class ClassTestConstrained1 {
    public int number = 9;
}

class ClassTestConstrained2 {
    public String str = "Alex";
}

class ConstrainedAnalyzerTest {
    ClassTestConstrained1 objNull1;
    ClassTestConstrained2 objNull2;
    ClassTestConstrained1 obj1 = new ClassTestConstrained1();
    ClassTestConstrained2 obj2 = new ClassTestConstrained2();

    @Test
    @DisplayName("Объект null под аннотацией Constrained")
    void constrainedForNullObject() {
        assertFalse(ConstrainedAnalyzer.checkAnnotation(objNull1));
        assertFalse(ConstrainedAnalyzer.checkAnnotation(objNull2));
    }

    @Test
    @DisplayName("Тип подвергается проверке так как помечен аннотацией Constrained")
    void constrainedExist() {
        assertTrue(ConstrainedAnalyzer.checkAnnotation(obj1));
    }

    @Test
    @DisplayName("Тип подвергается проверке так как не помечен аннотацией Constrained")
    void constrainedNotExist() {
        assertFalse(ConstrainedAnalyzer.checkAnnotation(obj2));
    }
}
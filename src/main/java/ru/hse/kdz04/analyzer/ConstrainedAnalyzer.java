package ru.hse.kdz04.analyzer;

import ru.hse.kdz04.annotation.Constrained;


public class ConstrainedAnalyzer {
    /**
     * Проверяет стоит ли аннотация на reference типе
     *
     * @param obj Объект, для класса которого происходит проверка
     * @return true - аннотация содержится, иначе false
     */
    public static boolean checkAnnotation(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj.getClass().isAnnotationPresent(Constrained.class);
    }
}

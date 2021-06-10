package ru.hse.kdz04.validator;

import ru.hse.kdz04.IncorrectAnnotatedTypeException;

import java.util.Set;

public interface Validator {
    /**
     * Получает все ошибки, полученные в процессе валидации объекта
     *
     * @param object Объект, который нужно провалидировать
     * @return Set<ValidationError>, которые не прошли условия поставленных аннотаций
     * @throws IllegalAccessException          возникает, когда приложение пытается рефлексивно создать
     *                                         экземпляр (отличный от массива), установить или получить
     *                                         поле или вызвать метод, но выполняемый в данный момент
     *                                         метод не имеет доступа к определению указанного класса,
     *                                         поля, метода или конструктора
     * @throws IncorrectAnnotatedTypeException возникает в результате применении
     *                                         аннотации к некорректному типу
     */
    Set<ValidationError> validate(Object object) throws IllegalAccessException, IncorrectAnnotatedTypeException;
}

package ru.hse.kdz04.validator;

import ru.hse.kdz04.IncorrectAnnotatedTypeException;
import ru.hse.kdz04.analyzer.*;
import ru.hse.kdz04.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class ValidatorClass implements Validator {
    // Словарь, где по аннотации лежит её анализатор
    private final Map<Class<? extends Annotation>, Analyzer> mapAnalyzers = Map.of(
            AnyOf.class, new AnyOfAnalyzer(),
            InRange.class, new InRangeAnalyzer(),
            Negative.class, new NegativeAnalyzer(),
            NotBlank.class, new NotBlankAnalyzer(),
            NotEmpty.class, new NotEmptyAnalyzer(),
            NotNull.class, new NotNullAnalyzer(),
            Positive.class, new PositiveAnalyzer(),
            Size.class, new SizeAnalyzer()
    );


    @Override
    public Set<ValidationError> validate(Object obj) throws IllegalAccessException, IncorrectAnnotatedTypeException {
        // Проверка на то, что объект подвергается проверке
        if (ConstrainedAnalyzer.checkAnnotation(obj)) {
            return validateFieldsInClass(obj, "");
        }
        // Если не подвергается, то ошибок нет
        return Collections.emptySet();
    }


    /**
     * Отправляет на валидацию все поля текущего класса и отдает список ошибок
     *
     * @param object объект, проходящий валидацию
     * @param path   путь до ошибки
     * @return сет найденных ошибок со всей информацией
     * @throws IllegalAccessException          возникает, когда приложение пытается рефлексивно создать
     *                                         экземпляр (отличный от массива), установить или получить
     *                                         поле или вызвать метод, но выполняемый в данный момент
     *                                         метод не имеет доступа к определению указанного класса,
     *                                         поля, метода или конструктора
     * @throws IncorrectAnnotatedTypeException возникает в результате применении
     *                                         аннотации к некорректному типу
     */
    private Set<ValidationError> validateFieldsInClass(Object object, String path)
            throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Set<ValidationError> validationErrors = new LinkedHashSet<>(Collections.emptySet());
        // Если мы заходим сюда повторно, значит попали на рекурсию для поля => ставим точку как разделитель
        if (!path.equals("")) {
            path += ".";
        }
        // Возвращаем все поля из класса, независимо от модификаторов доступа, исключая поля из класса родителя
        Field[] fields = object.getClass().getDeclaredFields();
        // Цикл по полям
        for (Field field : fields) {
            // Если поле приватное или протектед или пакет открываем доступ с помощью setAccessible(true)
            field.setAccessible(true);
            // Получаем значение поля
            Object fieldValue = field.get(object);
            // Получаем аннотированный тип поля
            AnnotatedType annotatedField = field.getAnnotatedType();
            // Получаем название поля
            String fieldName = field.getName();
            validationErrors.addAll(validateNextClass(fieldValue, annotatedField, path + fieldName));
        }
        return validationErrors;
    }

    /**
     * Отправляет повторно на валидацию поля типы которых классы с аннотацией Constrained
     * и валидирует элементы листа
     *
     * @param obj  объект, проходящий валидацию
     * @param type аннотированный тип
     * @param path путь до ошибки
     * @return сет найденных ошибок со всей информацией
     * @throws IllegalAccessException          возникает, когда приложение пытается рефлексивно создать
     *                                         экземпляр (отличный от массива), установить или получить
     *                                         поле или вызвать метод, но выполняемый в данный момент
     *                                         метод не имеет доступа к определению указанного класса,
     *                                         поля, метода или конструктора
     * @throws IncorrectAnnotatedTypeException возникает в результате применении
     *                                         аннотации к некорректному типу
     */
    private Set<ValidationError> validateNextClass(Object obj, AnnotatedType type, String path)
            throws IllegalAccessException, IncorrectAnnotatedTypeException {
        Set<ValidationError> validationErrors = new LinkedHashSet<>(Collections.emptySet());
        validationErrors.addAll(validateField(type, obj, path));
        // Если рассматриваемое поле имеет тип-класс, запускаем повторно метод валидации
        if (ConstrainedAnalyzer.checkAnnotation(obj)) {
            validationErrors.addAll(validateFieldsInClass(obj, path));
        }

        // Если объект лист, то проверяем его элементы по аннотациям
        if (obj instanceof List) {
            // Берем аннотации внутри параметров листа
            AnnotatedType[] annotatedTypes = ((AnnotatedParameterizedType) type).getAnnotatedActualTypeArguments();
            List<?> lst = (List<?>) obj;
            String str;
            for (int i = 0; i < lst.size(); i++) {
                str = path + "[" + i + "]";
                validationErrors.addAll(validateNextClass(lst.get(i), annotatedTypes[0], str));
            }
        }
        return validationErrors;
    }

    /**
     * Проверяет все аннотации у поля и записывает в сет ошибок, если такие находятся
     *
     * @param type аннотированный тип
     * @param obj  Объект для валидации
     * @param path Путь до ошибки
     * @return сет найденных ошибок со всей информацией
     * @throws IncorrectAnnotatedTypeException возникает в результате применении
     *                                         аннотации к некорректному типу
     */
    private Set<ValidationError> validateField(AnnotatedType type, Object obj, String path)
            throws IncorrectAnnotatedTypeException {
        // Берем все аннотации у аннотированного типа
        Annotation[] annotations = type.getAnnotations();
        Set<ValidationError> validationErrors = new LinkedHashSet<>(Collections.emptySet());
        // Пробегаем по каждой аннотации и валидируем относительно нее объект
        for (Annotation annotation : annotations) {
            mapAnalyzers.get(annotation.annotationType())
                    .analyzeObject(type, obj, path)
                    .ifPresent(validationErrors::add);
        }
        return validationErrors;
    }
}

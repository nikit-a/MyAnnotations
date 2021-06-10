package ru.hse.kdz04.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hse.kdz04.IncorrectAnnotatedTypeException;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorClassTest {

    @Test
    @DisplayName("Игнорирование других аннотаций при отстутствии Constrained")
    void notExistConstrainedAnnotation() throws IncorrectAnnotatedTypeException, IllegalAccessException {
        ValidatorClass validator = new ValidatorClass();
        CarForm car = new CarForm(-1, "Mers");
        Set<ValidationError> validationErrors = validator.validate(car);
        assertEquals(Set.of(), validationErrors);

    }

    @Test
    @DisplayName("Рассмотрение аннотаций у private/protected полей")
    void closedModifiersWithAnnotation() throws IncorrectAnnotatedTypeException, IllegalAccessException {
        ValidatorClass validator = new ValidatorClass();
        HouseForm houseForm = new HouseForm(5, List.of("9", "8", "7"));
        Set<ValidationError> validationErrors = validator.validate(houseForm);
        Iterator<ValidationError> it = validationErrors.iterator();
        ValidationError error = it.next();
        assertEquals(5, error.getFailedValue());
        assertEquals("room", error.getPath());
        assertEquals("У числа 5 не отрицательное значение", error.getMessage());
        ValidationError error2 = it.next();
        assertEquals(List.of("9", "8", "7"), error2.getFailedValue());
        assertEquals("floors", error2.getPath());
        String strExp = "Размер аннотированного элемента 3 не входит в диапазон [0;2]";
        assertEquals(strExp, error2.getMessage());

    }

    @Test
    @DisplayName("Пример тестируемой формы из 3 классов")
    void myUniversityForm() throws IncorrectAnnotatedTypeException, IllegalAccessException {
        ValidatorClass validator = new ValidatorClass();
        StudentForm student1 = new StudentForm("Petr", "  ", List.of(1, -2, 5, 7, 8, 4));
        StudentForm student2 = new StudentForm("Alex", null, List.of(5, 7, 8, 4));
        StudentForm student3 = new StudentForm("Misha", "K", List.of(5, 7, 8, 4, 3));
        StudentForm student4 = new StudentForm("Egor", "loop", List.of(-3, 3, 8, 4));
        StudentForm student5 = new StudentForm("Mark", "Eliel", List.of(9, 7, 0, 4));
        StudentForm student6 = new StudentForm("Nikita", " ", List.of(5, 7, 8, 4));
        StudentForm student7 = new StudentForm("Jek", "Miguel", List.of());
        List<StudentForm> students = List.of(student1, student2, student3,
                student4, student5, student6, student7);
        GroupForm groupForm = new GroupForm(students, 100);
        List<GroupForm> groups = List.of(groupForm);
        UniversityForm myUniversityForm = new UniversityForm("GUU", groups);

        Set<ValidationError> validationErrors = validator.validate(myUniversityForm);
        Iterator<ValidationError> it = validationErrors.iterator();
        ValidationError error = it.next();
        assertEquals("GUU", error.getFailedValue());
        assertEquals("nameUniversity", error.getPath());
        assertEquals("Cтрока GUU не совпадает ни с одной подстрокой из массива:" +
                " [HSE, MGU, MAI]", error.getMessage());

        ValidationError error2 = it.next();
        List<?> er = (List<?>) error2.getFailedValue();
        assertEquals(7, er.size());
        assertEquals("lstGroups[0].students", error2.getPath());
        String strExp = "Размер аннотированного элемента 7 не входит в диапазон [1;6]";
        assertEquals(strExp, error2.getMessage());

        ValidationError error3 = it.next();
        assertEquals("Petr", error3.getFailedValue());
        assertEquals("lstGroups[0].students[0].firstName", error3.getPath());
        assertEquals("Cтрока Petr не совпадает ни с одной подстрокой из массива: " +
                "[Alex, Egor, Misha, Nikita, Mark]", error3.getMessage());
        ValidationError error4 = it.next();
        assertEquals("  ", error4.getFailedValue());
        assertEquals("lstGroups[0].students[0].secondName", error4.getPath());
        assertEquals("Значение не должно быть пустым или содержать white space codepoints",
                error4.getMessage());

        ValidationError error5 = it.next();
        assertEquals("  ", error5.getFailedValue());
        assertEquals("lstGroups[0].students[0].secondName", error5.getPath());
        assertEquals("Размер аннотированного элемента 2 не входит в диапазон [4;15]",
                error5.getMessage());

        ValidationError error6 = it.next();
        assertEquals(List.of(1, -2, 5, 7, 8, 4), error6.getFailedValue());
        assertEquals("lstGroups[0].students[0].marks", error6.getPath());
        assertEquals("Размер аннотированного элемента 6 не входит в диапазон [1;5]",
                error6.getMessage());

        ValidationError error7 = it.next();
        assertEquals(-2, error7.getFailedValue());
        assertEquals("lstGroups[0].students[0].marks[1]", error7.getPath());
        assertEquals("У числа -2 не положительное значение", error7.getMessage());

        ValidationError error8 = it.next();
        assertNull(error8.getFailedValue());
        assertEquals("lstGroups[0].students[1].secondName", error8.getPath());
        assertEquals("Значение не должно быть null", error8.getMessage());

        ValidationError error9 = it.next();
        assertEquals("K", error9.getFailedValue());
        assertEquals("lstGroups[0].students[2].secondName", error9.getPath());
        assertEquals("Размер аннотированного элемента 1 не входит в диапазон [4;15]",
                error9.getMessage());

        ValidationError error10 = it.next();
        assertEquals(-3, error10.getFailedValue());
        assertEquals("lstGroups[0].students[3].marks[0]", error10.getPath());
        assertEquals("У числа -3 не положительное значение", error10.getMessage());

        ValidationError error11 = it.next();
        assertEquals(0, error11.getFailedValue());
        assertEquals("lstGroups[0].students[4].marks[2]", error11.getPath());
        assertEquals("У числа 0 не положительное значение", error11.getMessage());

        ValidationError error12 = it.next();
        assertEquals(" ", error12.getFailedValue());
        assertEquals("lstGroups[0].students[5].secondName", error12.getPath());
        assertEquals("Значение не должно быть пустым или содержать white space codepoints",
                error12.getMessage());

        ValidationError error13 = it.next();
        assertEquals(" ", error13.getFailedValue());
        assertEquals("lstGroups[0].students[5].secondName", error13.getPath());
        assertEquals("Размер аннотированного элемента 1 не входит в диапазон [4;15]",
                error13.getMessage());

        ValidationError error14 = it.next();
        assertEquals("Jek", error14.getFailedValue());
        assertEquals("lstGroups[0].students[6].firstName", error14.getPath());
        assertEquals("Cтрока Jek не совпадает ни с одной подстрокой из массива:" +
                " [Alex, Egor, Misha, Nikita, Mark]", error14.getMessage());

        ValidationError error15 = it.next();
        assertEquals(List.of(), error15.getFailedValue());
        assertEquals("lstGroups[0].students[6].marks", error15.getPath());
        assertEquals("Значение не должно быть пустым", error15.getMessage());

        ValidationError error16 = it.next();
        assertEquals(List.of(), error16.getFailedValue());
        assertEquals("lstGroups[0].students[6].marks", error16.getPath());
        assertEquals("Размер аннотированного элемента 0 не входит в диапазон [1;5]",
                error16.getMessage());

        ValidationError error17 = it.next();
        assertEquals(100, error17.getFailedValue());
        assertEquals("lstGroups[0].numberGroup", error17.getPath());
        assertEquals("Число 100 не входит в диапазон [191;199]", error17.getMessage());
    }
}
package ru.hse.kdz04.validator;

import ru.hse.kdz04.annotation.*;

import java.util.List;

@Constrained
public class StudentForm {
    @NotNull
    @AnyOf(str = {"Alex", "Egor", "Misha", "Nikita", "Mark"})
    private final String firstName;
    @NotNull
    @NotBlank
    @Size(min = 4, max = 15)
    private final String secondName;
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 5)
    private final List<@Positive Integer> marks;

    public StudentForm(String firstName, String secondName, List<Integer> marks) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.marks = marks;
    }
}

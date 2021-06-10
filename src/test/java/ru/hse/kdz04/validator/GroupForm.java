package ru.hse.kdz04.validator;

import ru.hse.kdz04.annotation.*;

import java.util.List;

@Constrained
public class GroupForm {
    @Size(min = 1, max = 6)
    @NotNull
    private final List<StudentForm> students;
    @Positive
    @InRange(min = 191, max = 199)
    private final int numberGroup;

    public GroupForm(List<StudentForm> students, int numberGroup) {
        this.numberGroup = numberGroup;
        this.students = students;
    }
}

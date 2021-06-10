package ru.hse.kdz04.validator;

import ru.hse.kdz04.annotation.AnyOf;
import ru.hse.kdz04.annotation.Constrained;
import ru.hse.kdz04.annotation.NotNull;

import java.util.List;

@Constrained
public class UniversityForm {
    @AnyOf(str = {"HSE", "MGU", "MAI"})
    private final String nameUniversity;
    private final List<@NotNull GroupForm> lstGroups;

    public UniversityForm(String nameUniversity, List<GroupForm> lstGroups) {
        this.lstGroups = lstGroups;
        this.nameUniversity = nameUniversity;
    }
}

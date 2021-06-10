package ru.hse.kdz04.validator;

import ru.hse.kdz04.annotation.AnyOf;
import ru.hse.kdz04.annotation.Constrained;
import ru.hse.kdz04.annotation.Negative;
import ru.hse.kdz04.annotation.Size;

import java.util.List;

@Constrained
public class HouseForm {

    @Negative
    private final int room;
    @Size(min = 0, max = 2)
    protected final List<String> floors;

    public HouseForm(int room, List<String> floors) {
        this.room = room;
        this.floors = floors;
    }
}

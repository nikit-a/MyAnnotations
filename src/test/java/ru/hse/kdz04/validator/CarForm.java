package ru.hse.kdz04.validator;

import ru.hse.kdz04.annotation.AnyOf;
import ru.hse.kdz04.annotation.Positive;

public class CarForm {
    @Positive
    private final int number;
    @AnyOf(str = {"BMW", "Honda"})
    private final String model;

    public CarForm(int number, String model) {
        this.number = number;
        this.model = model;
    }
}

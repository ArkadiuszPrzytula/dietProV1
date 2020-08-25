package com.pl.arkadiusz.diet_pro.model.entities.enums;

public enum Sex {
    MALE('M'), FEMALE('F'), OTHER('O');
    private final char abbreviation;

    Sex(char abbreviation) {
        this.abbreviation = abbreviation;
    }

    public char getAbbreviation() {
        return abbreviation;
    }
}

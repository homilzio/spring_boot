package com.training.springbootbuyitem.customvalidators;

import com.training.springbootbuyitem.customvalidators.interfaces.IBirthDateValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class BirthDateValidator implements ConstraintValidator<IBirthDateValidator, String> {

    private String FORMAT_STRING = "dd/MM/yyyy";
    private final DateTimeFormatter FMT = new DateTimeFormatterBuilder()
            .appendPattern(FORMAT_STRING)
            .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
            .toFormatter()
            .withZone(ZoneId.of("UTC"));

    @Override
    public void initialize(IBirthDateValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (value.split("/")[2].length() != FORMAT_STRING.split("/")[2].length())
                return false;
            FMT.parse(value, Instant::from);
        } catch (Exception e){
            return false;
        }
        return true;
    }


}

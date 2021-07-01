package com.training.springbootbuyitem.entity.request;

import com.training.springbootbuyitem.customvalidators.interfaces.IBirthDateValidator;

public class UpdateUserRequestDto {

    private String name;
    private String username;
    private Integer age;
    private String state;
    @IBirthDateValidator
    private String dateOfBirth;


}

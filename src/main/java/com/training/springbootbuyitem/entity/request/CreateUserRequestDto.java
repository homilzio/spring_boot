package com.training.springbootbuyitem.entity.request;

import com.training.springbootbuyitem.customvalidators.interfaces.IBirthDateValidator;
import com.training.springbootbuyitem.entity.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {

    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String username;
    private Integer age;
    private String state;

    @NotNull
    private String password;

    @IBirthDateValidator
    private String dateOfBirth;


    private Set<Role> roles;


}

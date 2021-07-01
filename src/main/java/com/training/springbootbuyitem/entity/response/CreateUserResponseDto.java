package com.training.springbootbuyitem.entity.response;

import com.training.springbootbuyitem.entity.model.Role;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponseDto {

    private Long personId;
    private String name;
    private int age;
    private String username;
    private String state;

    private Set<Role> roles;

    private Instant dateOfBirth;


}

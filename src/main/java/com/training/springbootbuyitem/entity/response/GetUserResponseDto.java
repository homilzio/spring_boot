package com.training.springbootbuyitem.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponseDto extends CreateUserResponseDto {

    private Long personId;
    private String name;
    private int age;
    private String username;
    private String state;

    private Instant dateOfBirth;

}

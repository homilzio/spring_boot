package com.training.springbootbuyitem.entity.response;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserResponseDto {
    private Long personId;
    private String name;
    private int age;
    private String username;
    private String state;

    private Instant dateOfBirth;

}

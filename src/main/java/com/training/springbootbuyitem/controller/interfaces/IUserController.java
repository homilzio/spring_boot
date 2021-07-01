package com.training.springbootbuyitem.controller.interfaces;

import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.entity.request.CreateUserRequestDto;
import com.training.springbootbuyitem.entity.response.GetUserResponseDto;
import com.training.springbootbuyitem.entity.response.UpdateUserResponseDto;
import com.training.springbootbuyitem.utils.annotation.ServiceOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface IUserController {

    @PostMapping("users/createUser")
    @ServiceOperation("createUser")
    ResponseEntity<CreateUserRequestDto> createUser(@RequestBody @Valid CreateUserRequestDto request) throws Exception;

    @GetMapping("/users/allUsers")
    @ServiceOperation("listUsers")
    ResponseEntity<List<GetUserResponseDto>> listUsers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                                       @RequestParam(defaultValue = "name") String sortBy);

    @GetMapping("/users/updateUser/{id}")
    @ServiceOperation("updateUser")
    ResponseEntity<UpdateUserResponseDto> updateUser(@PathVariable("id") Long id, @RequestBody User user);


}
